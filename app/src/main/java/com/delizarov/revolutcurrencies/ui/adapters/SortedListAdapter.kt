package com.delizarov.revolutcurrencies.ui.adapters

import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.view.View

typealias ViewHolderBinder<T> = (T, View) -> Unit

abstract class SortedListAdapter<T>(itemClass: Class<T>, comparator: Comparator<T>?, private val onBindItem: ViewHolderBinder<T>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val sortedItems: SortedList<T>
    var comparator: Comparator<T>? = comparator
        set(value) {

            if (value == field)
                return

            field = value
            refresh()
        }


    init {
        sortedItems = SortedList(itemClass, SortedListCallback())
    }

    override fun getItemCount(): Int = sortedItems.size()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = onBindItem(get(position), holder.itemView)

    fun areItemsTheSame(item1: T, item2: T): Boolean = item1 == item2

    fun areContentsTheSame(oldItem: T, newItem: T): Boolean = areItemsTheSame(oldItem, newItem)

    fun get(position: Int): T = sortedItems.get(position)

    fun add(item: T) = sortedItems.add(item)

    fun indexOf(item: T) = sortedItems.indexOf(item)

    fun updateItemAt(position: Int, item: T) = sortedItems.updateItemAt(position, item)

    fun addAll(items: Iterable<T>) {

        this.sortedItems.beginBatchedUpdates()
        for (item in items)
            this.sortedItems.add(item)
        this.sortedItems.endBatchedUpdates()
    }

    fun remove(item: T) = sortedItems.remove(item)

    fun remove(items: List<T>) {
        this.sortedItems.beginBatchedUpdates()
        for (item in items)
            this.sortedItems.remove(item)
        this.sortedItems.endBatchedUpdates()
    }

    fun clear() = sortedItems.clear()

    private fun refresh() {

        val copy = arrayListOf<T>()

        for (i in 0 until sortedItems.size())
            copy.add(sortedItems.get(i))

        sortedItems.clear()
        sortedItems.addAll(copy)
    }

    fun updateItems(items: List<T>) {

        this.sortedItems.beginBatchedUpdates()
        repeat(items.size) { pos ->
            sortedItems.updateItemAt(pos, items[pos])
        }
        this.sortedItems.endBatchedUpdates()
    }


    private inner class SortedListCallback : SortedList.Callback<T>() {

        override fun compare(o1: T, o2: T): Int = comparator?.compare(o1, o2) ?: 0

        override fun onInserted(position: Int, count: Int) = notifyItemRangeInserted(position, count)

        override fun areItemsTheSame(item1: T, item2: T): Boolean = this@SortedListAdapter.areItemsTheSame(item1, item2)

        override fun onMoved(fromPosition: Int, toPosition: Int) = notifyItemMoved(fromPosition, toPosition)

        override fun onChanged(position: Int, count: Int) = notifyItemRangeChanged(position, count)

        override fun onRemoved(position: Int, count: Int) = notifyItemRangeRemoved(position, count)

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = this@SortedListAdapter.areContentsTheSame(oldItem, newItem)

    }
}