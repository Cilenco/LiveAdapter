package com.cilenco.liveadapter.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.SortedList

import com.cilenco.liveadapter.ItemProvider
import com.cilenco.liveadapter.callbacks.SimpleSwipeCallback
import com.cilenco.liveadapter.callbacks.SwipeAdapter
import com.cilenco.liveadapter.utils.SortOrder
import com.cilenco.liveadapter.callbacks.SortedListCallback
import androidx.recyclerview.widget.ItemTouchHelper

open class LiveAdapter<T:Any>(private val items: ObservableList<T>): Adapter<BindingHolder<ViewDataBinding>>(), SwipeAdapter<T> {

    protected val itemProvider by lazy { ItemProvider(this, items) }
    protected val visibleItems by lazy { SortedList<T>(items[0].javaClass, listCallback) }

    protected val touchHelper by lazy { ItemTouchHelper(swipeHelper) }
    protected val swipeHelper by lazy { SimpleSwipeCallback(this) }
    protected val listCallback by lazy { SortedListCallback(this) }

    protected var recyclerView: RecyclerView? = null

    protected var onClickCallback: (view: View, item: T, position: Int) -> Unit = { _: View, _: T, _: Int ->  }
    protected var onLongClickCallback: (view: View, item: T, position: Int) -> Boolean = { _: View, _: T, _: Int ->  false }

    @LayoutRes protected var layoutRes: Int = 0
    @IdRes protected var layoutVar: Int = 0

    override var swipeDirections: (position: Int) -> Int
        set(value) { swipeHelper.swipeDirections = value }
        get() { return swipeHelper.swipeDirections }

    override var dragDirections: (position: Int) -> Int
        set(value) { swipeHelper.dragDirections = value }
        get() { return swipeHelper.dragDirections }

    override var onSwipeCallback: (position:Int, item: T, direction: Int) -> Unit
        set(value) { swipeHelper.onSwipeCallback = value }
        get() { return swipeHelper.onSwipeCallback }

    override var onDragCallback: (oldPosition: Int, newPosition: Int, dropped: Boolean) -> Boolean
        set(value) { swipeHelper.onDragCallback = value }
        get() { return swipeHelper.onDragCallback }

    override var swipeColorLeft: Int
        set(value) { swipeHelper.swipeColorLeft = value }
        get() { return swipeHelper.swipeColorLeft }

    override var swipeColorRight: Int
        set(value) { swipeHelper.swipeColorRight = value }
        get() { return swipeHelper.swipeColorRight }

    override var swipeDrawableLeft: Drawable?
        set(value) { swipeHelper.swipeDrawableLeft = value }
        get() { return swipeHelper.swipeDrawableLeft }

    override var swipeDrawableRight: Drawable?
        set(value) { swipeHelper.swipeDrawableRight = value }
        get() { return swipeHelper.swipeDrawableRight }

    override var swipeMargin: Int
        set(value) { swipeHelper.swipeMargin = value }
        get() { return swipeHelper.swipeMargin }

    var sortOrder = SortOrder.ASC
        set(value) { field = value; reorderList() }

    init {
        listCallback.selector = { sortOrder.value * items.indexOf(it) }
        itemProvider.filter(null)
    }

    fun setup(@LayoutRes layoutRes: Int, @IdRes variable: Int) {
        this.layoutRes = layoutRes
        this.layoutVar = variable
    }

    fun filter(predicate: (T) -> Boolean) {
        itemProvider.predicate = predicate
    }

    fun clearFilter() {
        itemProvider.predicate = { true }
    }

    fun <C : Comparable<C>> sortedBy(selector: (T) -> C) {
        listCallback.selector = selector
        reorderList()
    }

    fun clearOrder() {
        sortedBy { items.indexOf(it) }
    }

    private fun reorderList() {
        visibleItems.beginBatchedUpdates()

        val copy = (visibleItems.size() - 1 downTo 0).map { visibleItems.removeItemAt(it) }
        copy.forEach { visibleItems.add(it) }

        visibleItems.endBatchedUpdates()
    }

    fun onClick(callback: (view: View, item: T, position: Int) -> Unit) {
        onClickCallback = callback
    }

    fun onLongClick(callback: (view: View, item: T, position: Int) -> Boolean) {
        onLongClickCallback = callback
    }

    override fun getItemId(position: Int): Long {
        return visibleItems[position].hashCode().toLong()
        //return super.getItemId(position)
    }

    override fun onBindViewHolder(holder: BindingHolder<ViewDataBinding>, position: Int) {
        holder.binding.setVariable(layoutVar, getItem(position))
        holder.binding.executePendingBindings()

        holder.itemView.setOnClickListener { onClickCallback(holder.itemView, getItem(position), position) }
        holder.itemView.setOnLongClickListener { onLongClickCallback(holder.itemView, getItem(position), position) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ViewDataBinding> {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, viewType, parent, false)

        return BindingHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return layoutRes
    }

    internal fun setVisibleItems(items: Collection<T>) {
        visibleItems.beginBatchedUpdates()

        for (i in visibleItems.size() - 1 downTo 0) {
            val item = visibleItems[i]
            if (items.contains(item)) continue
            visibleItems.remove(item)
        }

        visibleItems.addAll(items)
        visibleItems.endBatchedUpdates()
    }

    fun getItem(position: Int): T {
        return visibleItems[position]
    }

    override fun getItemCount(): Int {
        return visibleItems.size()
    }

    override fun onAttachedToRecyclerView(rv: RecyclerView) {
        recyclerView = rv // Indicate attached to RecyclerView

        itemProvider.startObserving()
        touchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(rv: RecyclerView) {
        recyclerView = null // Indicate detached from RecyclerView
        itemProvider.endObserving()
    }
}