package com.nestoleh.bottomsheetspinner.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nestoleh.bottomsheetspinner.R
import com.nestoleh.bottomsheetspinner.adapter.BottomSheetSpinnerAdapter
import kotlinx.android.synthetic.main.dialog_spinner_bottom_menu.*

/**
 * Bottom sheet menu dialog
 *
 * @author oleg.nestyuk
 */
internal class SpinnerBottomMenuDialogFragment : BottomSheetDialogFragment() {
    var adapter: BottomSheetSpinnerAdapter<*, *>? = null
        set(value) {
            field = value
            if (isAdded) {
                initRecyclerView()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetSpinner_DialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        makeFullHeight()
    }

    private fun initRecyclerView() {
        menuRecyclerView?.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(LAYOUT, container, false)
    }

    private fun makeFullHeight() {
        dialog?.let { dialog ->
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                bottomSheet.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

                view?.let { view ->
                    view.post {
                        val parent = view.parent as View
                        val params = parent.layoutParams as CoordinatorLayout.LayoutParams
                        val bottomSheetBehavior = params.behavior as BottomSheetBehavior
                        bottomSheetBehavior.peekHeight = view.measuredHeight
                        (bottomSheet.parent as View).setBackgroundColor(Color.TRANSPARENT)
                    }
                }
            }
        }
    }

    fun showAllowingStateLost(manager: FragmentManager, tag: String? = null) {
        if (!this.isAdded) {
            val transaction = manager.beginTransaction()
            transaction.add(this, tag)
            transaction.commitAllowingStateLoss()
        }
    }

    companion object {
        val LAYOUT: Int = R.layout.dialog_spinner_bottom_menu
    }
}