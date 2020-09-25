package com.nestoleh.bottomsheetspinner.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StyleRes
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

    @StyleRes
    private var dialogTheme: Int = DEFAULT_DIALOG_THEME

    var adapter: BottomSheetSpinnerAdapter<*, *>? = null
        set(value) {
            field = value
            if (isAdded) {
                initRecyclerView()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        setStyle(STYLE_NORMAL, dialogTheme)
    }

    private fun initData() {
        dialogTheme = arguments?.getInt(EXTRA_DIALOG_THEME) ?: DEFAULT_DIALOG_THEME
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

        val DEFAULT_DIALOG_THEME = R.style.BottomSheetSpinner_DialogTheme

        private const val EXTRA_DIALOG_THEME = "dialog_theme"

        fun newInstance(
            @StyleRes themeRes: Int = DEFAULT_DIALOG_THEME
        ): SpinnerBottomMenuDialogFragment {
            val fragment = SpinnerBottomMenuDialogFragment()
            fragment.arguments = Bundle().apply {
                putInt(EXTRA_DIALOG_THEME, themeRes)
            }
            return fragment
        }
    }
}