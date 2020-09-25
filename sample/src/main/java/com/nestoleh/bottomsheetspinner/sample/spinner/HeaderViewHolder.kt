package com.nestoleh.bottomsheetspinner.sample.spinner

import android.view.View
import android.widget.TextView
import com.nestoleh.bottomsheetspinner.adapter.BottomSheetSpinnerItemViewHolder
import com.nestoleh.bottomsheetspinner.sample.R

class HeaderViewHolder(
    itemView: View
) : BottomSheetSpinnerItemViewHolder(itemView, null) {

    private val name: TextView by lazy { itemView.findViewById(R.id.name) }

    fun bindHeader(header: String) {
        name.text = header
    }

    companion object {
        const val LAYOUT: Int = R.layout.item_header
    }
}