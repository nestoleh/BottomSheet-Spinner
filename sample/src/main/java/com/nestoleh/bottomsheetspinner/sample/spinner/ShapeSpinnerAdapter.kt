package com.nestoleh.bottomsheetspinner.sample.spinner

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nestoleh.bottomsheetspinner.adapter.BottomSheetSpinnerItemClickListener
import com.nestoleh.bottomsheetspinner.adapter.BottomSheetSpinnerItemViewHolder
import com.nestoleh.bottomsheetspinner.adapter.SimpleBottomSheetSpinnerAdapter
import com.nestoleh.bottomsheetspinner.sample.Shape

/**
 * Adapter for spinner with shapes
 *
 * @author oleg.nestyuk
 */
class ShapeSpinnerAdapter(
    entities: List<ShapeSpinnerEntity>? = null
) : SimpleBottomSheetSpinnerAdapter<ShapeSpinnerEntity, BottomSheetSpinnerItemViewHolder, BottomSheetSpinnerItemViewHolder>(
    entities
) {

    override fun onCreateSelectedViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetSpinnerItemViewHolder {
        return when (getTypeByViewType(viewType)) {
            ShapeSpinnerEntity.Type.HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(HeaderViewHolder.LAYOUT, parent, false)
                HeaderViewHolder(view)
            }
            ShapeSpinnerEntity.Type.ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(ShapeViewHolder.LAYOUT_SELECTED, parent, false)
                ShapeViewHolder(view, null)
            }
        }
    }

    override fun onCreateDropDownViewHolder(
        parent: ViewGroup,
        viewType: Int,
        itemClickListener: BottomSheetSpinnerItemClickListener
    ): BottomSheetSpinnerItemViewHolder {
        return when (getTypeByViewType(viewType)) {
            ShapeSpinnerEntity.Type.HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(HeaderViewHolder.LAYOUT, parent, false)
                HeaderViewHolder(view)
            }
            ShapeSpinnerEntity.Type.ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(ShapeViewHolder.LAYOUT_DROPDOWN, parent, false)
                ShapeViewHolder(view, itemClickListener)
            }
        }
    }

    override fun onBindSelectedViewHolder(holder: BottomSheetSpinnerItemViewHolder, position: Int) {
        onBindDropDownViewHolder(holder, position)
    }

    override fun onBindDropDownViewHolder(holder: BottomSheetSpinnerItemViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ShapeSpinnerEntity.Item -> bindShapeViewHolder(
                holder as ShapeViewHolder,
                item.value
            )
            is ShapeSpinnerEntity.Header -> bindHeaderViewHolder(
                holder as HeaderViewHolder,
                item.value
            )
        }
    }

    private fun bindHeaderViewHolder(holder: HeaderViewHolder, header: String) {
        holder.bindHeader(header)
    }

    private fun bindShapeViewHolder(holder: ShapeViewHolder, shape: Shape) {
        holder.bindShape(shape)
    }

    override fun getItemViewType(position: Int): Int {
        return getItemType(position).ordinal
    }

    override fun isEnabled(position: Int): Boolean {
        val type = getItemType(position)
        return type == ShapeSpinnerEntity.Type.ITEM
    }

    private fun getItemType(position: Int): ShapeSpinnerEntity.Type {
        return getItem(position).type
    }

    private fun getTypeByViewType(viewType: Int): ShapeSpinnerEntity.Type {
        return ShapeSpinnerEntity.Type.values()[viewType]
    }
}