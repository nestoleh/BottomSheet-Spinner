package com.nestoleh.bottomsheetspinner

/**
 * Callback interface to be invoked when item has been selected in spinner
 *
 * @author oleg.nestyuk
 */
interface OnBottomSheetSpinnerItemSelected {
    fun onItemSelected(position: Int)
    fun onNothingSelected()
}