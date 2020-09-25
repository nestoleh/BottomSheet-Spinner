package com.nestoleh.bottomsheetspinner.sample.spinner

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nestoleh.bottomsheetspinner.adapter.BottomSheetSpinnerAdapter
import com.nestoleh.bottomsheetspinner.adapter.BottomSheetSpinnerItemClickListener
import com.nestoleh.bottomsheetspinner.sample.Shape

/**
 * Adapter for spinner with shapes
 *
 * @author oleg.nestyuk
 */
class ShapeSpinnerAdapter(
    shapes: List<Shape>
) : BottomSheetSpinnerAdapter<ShapeViewHolder, ShapeViewHolder>() {
    private val shapes: ArrayList<Shape> = ArrayList()

    init {
        updateShapes(shapes)
    }

    fun updateShapes(shapes: List<Shape>) {
        this.shapes.clear()
        this.shapes.addAll(shapes)
        notifyDataSetChanged()
    }

    override fun onCreateSelectedViewHolder(parent: ViewGroup, viewType: Int): ShapeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(ShapeViewHolder.LAYOUT_SELECTED, parent, false)
        return ShapeViewHolder(view, null)
    }

    override fun onBindSelectedViewHolder(holder: ShapeViewHolder, position: Int) {
        val shape = shapes[position]
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
        val shape = shapes[position]
        holder.bindShape(shape)
    }

    override fun getItemCount(): Int {
        return shapes.size
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getItem(position: Int): T? {
        return shapes[position] as T
    }
}