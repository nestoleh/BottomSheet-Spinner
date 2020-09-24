package com.nestoleh.bottomsheetspinner.sample.spinner

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nestoleh.bottomsheetspinner.adapter.BottomSheetSpinnerAdapter
import com.nestoleh.bottomsheetspinner.sample.Shape

/**
 * Adapter for spinner with shapes
 *
 * @author oleg.nestyuk
 */
class ShapeSpinnerAdapter(
    shapes: List<Shape>
) : BottomSheetSpinnerAdapter<ShapeViewHolder>() {
    private val shapes: ArrayList<Shape> = ArrayList()

    init {
        updateShapes(shapes)
    }

    fun updateShapes(shapes: List<Shape>) {
        this.shapes.clear()
        this.shapes.addAll(shapes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShapeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(ShapeViewHolder.LAYOUT, parent, false)
        return ShapeViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: ShapeViewHolder, position: Int) {
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