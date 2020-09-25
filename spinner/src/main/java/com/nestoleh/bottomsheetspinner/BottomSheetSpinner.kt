package com.nestoleh.bottomsheetspinner

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View.OnClickListener
import android.widget.FrameLayout
import androidx.annotation.StyleRes
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

    private var adapter: BottomSheetSpinnerAdapter<*, *>? = null

    private val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            reinitSelectedPosition()
            updateDialogAdapter()
        }
    }

    private val onAdapterItemClickListener = BottomSheetSpinnerItemClickListener { position ->
        updateSelectedItem(position)
        dialog?.dismissAllowingStateLoss()
    }

    @StyleRes
    private var dialogTheme: Int = SpinnerBottomMenuDialogFragment.DEFAULT_DIALOG_THEME
    private var dialogTitle: String? = null
    private var dialogTag: String? = null
    private var dialog: SpinnerBottomMenuDialogFragment? = null

    private val fragmentManager: FragmentManager
        get() = (context as FragmentActivity).supportFragmentManager

    private val onClickListener: OnClickListener = OnClickListener {
        val localDialog =
            dialog ?: SpinnerBottomMenuDialogFragment.newInstance(dialogTheme, dialogTitle)
        localDialog.adapter = adapter
        localDialog.showAllowingStateLost(fragmentManager, provideDialogTag())
        dialog = localDialog
    }

    private var selectedPosition: Int = NO_POSITION

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet? = null) {
        initParameters(context, attrs)
        setOnClickListener(onClickListener)
    }

    private fun initParameters(context: Context, attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.BottomSheetSpinner, 0, 0
        )
        try {
            dialogTheme =
                typedArray.getResourceId(
                    R.styleable.BottomSheetSpinner_bss_dialogTheme,
                    dialogTheme
                )
            dialogTitle = typedArray.getString(R.styleable.BottomSheetSpinner_bss_dialogTitle)
        } finally {
            typedArray.recycle()
        }
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

    fun setAdapter(adapter: BottomSheetSpinnerAdapter<*, *>?) {
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
        if (isSelectedPositionValid() && adapter != null) {
            val position = selectedPosition
            val view = adapter.provideSelectedViewForPosition(this, position)
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
        val newPosition = if (isSelectedPositionValid()) {
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

    private fun isSelectedPositionValid(): Boolean {
        return isPositionCanBeSelected(selectedPosition)
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
        if (isPositionCanBeSelected(restoredSelectedPosition)) {
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
            adapter.getItem(selection) as T?
        } else {
            null
        }
    }

    fun getSelectedItemPosition(): Int {
        return selectedPosition
    }

    fun isPositionCanBeSelected(position: Int): Boolean {
        return position >= 0
                && position < (adapter?.itemCount ?: 0)
                && (adapter?.isEnabled(position) == true)
    }

    fun setSelection(position: Int) {
        val adapter = adapter
        if (adapter != null && isPositionCanBeSelected(position)) {
            updateSelectedItem(position)
        } else {
            throw IllegalArgumentException(
                "Selection position out of range or can't be selected (isEnabled == false)"
            )
        }
    }

    fun setSelectionIfPossible(position: Int): Boolean {
        return if (isPositionCanBeSelected(position)) {
            setSelection(position)
            true
        } else {
            false
        }
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