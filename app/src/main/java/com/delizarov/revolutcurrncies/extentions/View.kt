package com.delizarov.revolutcurrncies.extentions

import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View

private fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

fun <T : View> AppCompatActivity.bind(@IdRes id: Int) = unsafeLazy { findViewById<T>(id) }

fun <T : View> RecyclerView.ViewHolder.bind(@IdRes id: Int) = unsafeLazy { itemView.findViewById<T>(id) }

fun RecyclerView.ViewHolder.onClick(block: (View)-> Unit) = itemView.setOnClickListener { block.invoke(itemView) }