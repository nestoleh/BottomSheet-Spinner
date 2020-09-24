package com.nestoleh.bottomsheetspinner.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Base adapter to use as a source for bottom sheet spinner
 *
 * @author oleg.nestyuk
 */
abstract class BottomSheetSpinnerAdapter<VH : BottomSheetSpinnerItemViewHolder> :
    RecyclerView.Adapter<VH>() {

    private val clickListeners: HashSet<BottomSheetSpinnerItemClickListener> = HashSet()

    protected val itemClickListener = BottomSheetSpinnerItemClickListener { position ->
        if (isEnabled(position)) {
            clickListeners.forEach {
                it.onItemClicked(position)
            }
        }
    }

    fun addClickListener(l: BottomSheetSpinnerItemClickListener) {
        clickListeners.add(l)
    }

    fun removeClickListener(l: BottomSheetSpinnerItemClickListener) {
        clickListeners.remove(l)
    }

    fun provideViewForPosition(viewGroup: ViewGroup, position: Int): View {
        val viewType = getItemViewType(position)
        val viewHolder = onCreateViewHolder(viewGroup, viewType)
        onBindViewHolder(viewHolder, position)
        return viewHolder.itemView
    }

    open fun isEnabled(position: Int): Boolean {
        return true
    }

    abstract fun <T : Any> getItem(position: Int): T?
}