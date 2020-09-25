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
    private val onItemClickListener: BottomSheetSpinnerItemClickListener? = null
) : RecyclerView.ViewHolder(itemView) {

    init {
        if (onItemClickListener != null) {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClicked(position)
                }
            }
        }
    }
}