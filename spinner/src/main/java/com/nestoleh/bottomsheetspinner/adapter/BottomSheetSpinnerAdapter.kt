package com.nestoleh.bottomsheetspinner.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Base adapter to use as a source for bottom sheet spinner
 *
 * @author oleg.nestyuk
 */
abstract class BottomSheetSpinnerAdapter
<VHS : BottomSheetSpinnerItemViewHolder, VHD : BottomSheetSpinnerItemViewHolder> :
    RecyclerView.Adapter<BottomSheetSpinnerItemViewHolder>() {

    private val clickListeners: HashSet<BottomSheetSpinnerItemClickListener> = HashSet()

    private val itemClickListener = BottomSheetSpinnerItemClickListener { position ->
        if (isEnabled(position)) {
            clickListeners.forEach {
                it.onItemClicked(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHD {
        return onCreateDropDownViewHolder(parent, viewType, itemClickListener)
    }

    abstract fun onCreateSelectedViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VHS

    abstract fun onCreateDropDownViewHolder(
        parent: ViewGroup,
        viewType: Int,
        itemClickListener: BottomSheetSpinnerItemClickListener
    ): VHD

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: BottomSheetSpinnerItemViewHolder, position: Int) {
        onBindDropDownViewHolder(holder as VHD, position)
    }

    abstract fun onBindSelectedViewHolder(holder: VHS, position: Int)

    abstract fun onBindDropDownViewHolder(holder: VHD, position: Int)

    fun provideSelectedViewForPosition(viewGroup: ViewGroup, position: Int): View {
        val viewType = getItemViewType(position)
        val viewHolder = onCreateSelectedViewHolder(viewGroup, viewType)
        onBindSelectedViewHolder(viewHolder, position)
        return viewHolder.itemView
    }

    fun addClickListener(l: BottomSheetSpinnerItemClickListener) {
        clickListeners.add(l)
    }

    fun removeClickListener(l: BottomSheetSpinnerItemClickListener) {
        clickListeners.remove(l)
    }

    open fun isEnabled(position: Int): Boolean {
        return true
    }

    abstract fun getItem(position: Int): Any?
}