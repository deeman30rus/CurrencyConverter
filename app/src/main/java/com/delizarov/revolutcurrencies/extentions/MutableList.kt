package com.delizarov.revolutcurrencies.extentions

fun <T> MutableList<T>.bubbleUp(from: Int, to: Int) {
    for (i in from downTo to + 1) {
        this[i] = this[i - 1].also { this[i - 1] = this[i] }
    }
}