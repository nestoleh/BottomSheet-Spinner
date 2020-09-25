package com.nestoleh.bottomsheetspinner.sample.spinner

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nestoleh.bottomsheetspinner.adapter.BottomSheetSpinnerItemClickListener
import com.nestoleh.bottomsheetspinner.adapter.SimpleBottomSheetSpinnerAdapter
import com.nestoleh.bottomsheetspinner.sample.Shape

/**
 * Adapter for spinner with shapes
 *
 * @author oleg.nestyuk
 */
class ShapeSpinnerAdapter(
    shapes: List<Shape>? = null
) : SimpleBottomSheetSpinnerAdapter<Shape, ShapeViewHolder, ShapeViewHolder>(shapes) {

    override fun onCreateSelectedViewHolder(parent: ViewGroup, viewType: Int): ShapeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(ShapeViewHolder.LAYOUT_SELECTED, parent, false)
        return ShapeViewHolder(view, null)
    }

    override fun onBindSelectedViewHolder(holder: ShapeViewHolder, position: Int) {
        val shape = getItem(position)
        holder.bindShape(shape)
    }

    override fun onCreateDropDownViewHolder(
        parent: ViewGroup,
        viewType: Int,
        itemClickListener: BottomSheetSpinnerItemClickListener
    ): ShapeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(ShapeViewHolder.LAYOUT_DROPDOWN, parent, false)
        return ShapeViewHolder(view, itemClickListener)
    }

    override fun onBindDropDownViewHolder(holder: ShapeViewHolder, position: Int) {
        val shape = getItem(position)
        holder.bindShape(shape)
    }
}