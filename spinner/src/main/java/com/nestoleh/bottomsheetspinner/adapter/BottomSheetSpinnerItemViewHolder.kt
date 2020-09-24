package com.nestoleh.bottomsheetspinner.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Abstract class for bottom sheet spinner view holder
 *
 * @author oleg.nestyuk
 */
abstract class BottomSheetSpinnerItemViewHolder(
    itemView: View,
    private val onItemClickListener: BottomSheetSpinnerItemClickListener
) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClicked(position)
            }
        }
    }
}