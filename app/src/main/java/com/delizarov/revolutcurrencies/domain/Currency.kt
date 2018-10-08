package com.delizarov.revolutcurrencies.domain

typealias Currency1 = String

data class Currency(
        val currency: String,
        var amount: Float
)