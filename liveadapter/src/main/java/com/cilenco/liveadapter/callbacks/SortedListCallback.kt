package com.cilenco.liveadapter.callbacks

import androidx.recyclerview.widget.SortedList
import com.cilenco.liveadapter.adapters.LiveAdapter

class SortedListCallback<T:Any>(private val adapter: LiveAdapter<T>): SortedList.Callback<T>() {
    lateinit var selector: (T) -> Comparable<*>

    private val comp: Comparator<T> = Comparator {
            x: T, y: T -> adapter.sortOrder.value * compareValuesBy(x, y, selector)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        adapter.notifyItemMoved(fromPosition, toPosition)
    }

    override fun onChanged(position: Int, count: Int) {
        adapter.notifyItemRangeChanged(position, count)
    }

    override fun onInserted(position: Int, count: Int) {
        adapter.notifyItemRangeInserted(position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        adapter.notifyItemRangeRemoved(position, count)
    }

    override fun compare(t1: T, t2: T): Int {
        return comp.compare(t1, t2)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(item1: T, item2: T): Boolean {
        return item1 === item2
    }
}