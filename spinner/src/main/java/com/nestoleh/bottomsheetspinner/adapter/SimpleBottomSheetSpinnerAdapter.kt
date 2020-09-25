package com.nestoleh.bottomsheetspinner.adapter

/**
 * Base realization of bottom sheet spinner adapter based on list of elements
 *
 * @author oleg.nestyuk
 */
abstract class SimpleBottomSheetSpinnerAdapter
<T, VHS : BottomSheetSpinnerItemViewHolder, VHD : BottomSheetSpinnerItemViewHolder>(
    list: List<T>? = null
) : BottomSheetSpinnerAdapter<VHS, VHD>() {

    private val list: ArrayList<T> = ArrayList()

    init {
        setData(list)
    }

    private fun setData(newList: List<T>?) {
        this.list.clear()
        if (!newList.isNullOrEmpty()) {
            this.list.addAll(newList)
        }
        notifyDataSetChanged()
    }

    fun updateData(newList: List<T>?) {
        setData(newList)
    }

    override fun getItem(position: Int): T {
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }
}