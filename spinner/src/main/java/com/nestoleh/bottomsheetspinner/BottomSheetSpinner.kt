package com.nestoleh.bottomsheetspinner

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View.OnClickListener
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.nestoleh.bottomsheetspinner.adapter.BottomSheetSpinnerAdapter
import com.nestoleh.bottomsheetspinner.adapter.BottomSheetSpinnerItemClickListener
import com.nestoleh.bottomsheetspinner.dialog.SpinnerBottomMenuDialogFragment
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Bottom sheet spinner view
 *
 * @author oleg.nestyuk
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class BottomSheetSpinner : FrameLayout {

    var onItemSelectedListener: OnBottomSheetSpinnerItemSelected? = null

    private var adapter: BottomSheetSpinnerAdapter<*>? = null

    private val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            val position = if (!isSelectedPositionInMeasures()) {
                getFirstSelectablePosition()
            } else {
                selectedPosition
            }
            updateSelectedItem(position)
            updateDialogAdapter()
        }
    }

    private val onAdapterItemClickListener = BottomSheetSpinnerItemClickListener { position ->
        updateSelectedItem(position)
        dialog?.dismissAllowingStateLoss()
    }

    private var dialogTag: String? = null
    private var dialog: SpinnerBottomMenuDialogFragment? = null

    private val fragmentManager: FragmentManager
        get() = (context as FragmentActivity).supportFragmentManager

    private val onClickListener: OnClickListener = OnClickListener {
        val localDialog = dialog ?: SpinnerBottomMenuDialogFragment()
        localDialog.adapter = adapter
        localDialog.showAllowingStateLost(fragmentManager, provideDialogTag())
        dialog = localDialog
    }

    private var selectedPosition: Int = NO_POSITION

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        setOnClickListener(onClickListener)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initAdapterListeners()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        this.adapter?.removeClickListener(onAdapterItemClickListener)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }

    // adapter

    fun setAdapter(adapter: BottomSheetSpinnerAdapter<*>?) {
        resetAdapterListeners()
        this.adapter = adapter
        initAdapterListeners()
        reinitSelectedPosition()
        updateDialogAdapter()
    }

    private fun updateDialogAdapter() {
        if (adapter == null) {
            this.dialog?.dismissAllowingStateLoss()
        } else {
            this.dialog?.adapter = adapter
        }
    }

    private fun resetAdapterListeners() {
        this.adapter?.removeClickListener(onAdapterItemClickListener)
        try {
            adapter?.unregisterAdapterDataObserver(adapterDataObserver)
        } catch (e: Exception) {
        }
    }

    private fun initAdapterListeners() {
        adapter?.addClickListener(onAdapterItemClickListener)
        try {
            adapter?.unregisterAdapterDataObserver(adapterDataObserver)
        } catch (e: Exception) {
        }
        adapter?.registerAdapterDataObserver(adapterDataObserver)
    }

    // selection

    private fun updateSelectedView() {
        val adapter = adapter
        clearSelectedView()
        if (isSelectedPositionInMeasures() && adapter != null) {
            val position = selectedPosition
            val view = adapter.provideViewForPosition(this, position)
            view.isClickable = false
            view.isFocusable = false
            view.setOnClickListener(null)
            addView(view)
        } else {
            requestLayout()
        }
    }

    private fun clearSelectedView() {
        removeAllViewsInLayout()
    }

    private fun reinitSelectedPosition() {
        val newPosition = if (isSelectedPositionInMeasures()) {
            selectedPosition
        } else {
            getFirstSelectablePosition()
        }
        updateSelectedItem(newPosition)
    }

    private fun updateSelectedItem(position: Int) {
        selectedPosition = position
        if (selectedPosition >= 0) {
            onItemSelectedListener?.onItemSelected(position)
        } else {
            onItemSelectedListener?.onNothingSelected()
        }
        updateSelectedView()
    }

    // position

    private fun getFirstSelectablePosition(): Int {
        val adapter = adapter
        if (adapter != null) {
            for (i in 0 until adapter.itemCount) {
                if (adapter.isEnabled(i)) {
                    return i
                }
            }
        }
        return NO_POSITION
    }

    private fun isSelectedPositionInMeasures(): Boolean {
        return isPositionInMeasures(selectedPosition)
    }

    // dialog

    private fun provideDialogTag(): String {
        val tag = dialogTag ?: "${this.javaClass.name}_${UUID.randomUUID()}"
        dialogTag = tag
        return tag
    }

    // save & restore state

    override fun onSaveInstanceState(): Parcelable? {
        return State(
            parentState = super.onSaveInstanceState(),
            selectedPosition = selectedPosition,
            dialogTag = provideDialogTag()
        )
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val restoredState = state as? State
        if (restoredState != null) {
            super.onRestoreInstanceState(state.parentState)
            restoreSelectedPosition(restoredState.selectedPosition)
            restoreDialog(restoredState.dialogTag)
        }
    }

    private fun restoreSelectedPosition(restoredSelectedPosition: Int) {
        if (isPositionInMeasures(restoredSelectedPosition)) {
            updateSelectedItem(restoredSelectedPosition)
        }
    }

    private fun restoreDialog(restoredDialogTag: String?) {
        if (restoredDialogTag != null) {
            val dialog = fragmentManager.findFragmentByTag(restoredDialogTag)
                    as? SpinnerBottomMenuDialogFragment
            if (dialog != null && (dialog.dialog?.isShowing == true)) {
                dialogTag = restoredDialogTag
                this.dialog = dialog
                updateDialogAdapter()
            }
        }
    }

    // spinner

    fun getCount(): Int {
        return adapter?.itemCount ?: 0
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getSelectedItem(): T? {
        val adapter = adapter
        val selection = selectedPosition
        return if (adapter != null && adapter.itemCount > 0 && selection >= 0) {
            adapter.getItem(selection)
        } else {
            null
        }
    }

    fun getSelectedItemPosition(): Int {
        return selectedPosition
    }

    fun setSelection(position: Int) {
        val adapter = adapter
        if (adapter != null && position >= 0 && position < adapter.itemCount) {
            updateSelectedItem(position)
        } else {
            throw IllegalArgumentException("Selected position out of range")
        }
    }

    fun isPositionInMeasures(position: Int): Boolean {
        return position >= 0 && position < (adapter?.itemCount ?: 0)
    }


    @Parcelize
    private data class State(
        val parentState: Parcelable?,
        val selectedPosition: Int,
        val dialogTag: String?
    ) : Parcelable

    companion object {
        const val NO_POSITION: Int = -1
    }
}