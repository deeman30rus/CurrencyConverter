package com.delizarov.revolutcurrencies.domain

import com.delizarov.revolutcurrencies.extentions.bubbleUp
import io.reactivex.subjects.PublishSubject

class CurrencyList(
        rates: ExchangeRates,
        initialValue: Float = 100f
) {

    val observable = PublishSubject.create<IntRange>()

    var rates: ExchangeRates = rates
        set(value) {

            field = value

            updateValues()
        }

    var baseCurrency: String
        get() = base.currency
        set(value) {

            val currency = list.find { it.currency == value }
            val pos = list.indexOf(currency)

            list.bubbleUp(pos, 0)

            observable.onNext(IntRange(0, pos))
        }

    var baseAmount: Float
        get() = base.amount
        set(value) {

            base.amount = value

            updateValues()
        }

    private val base: Currency
        get() = list[0]

    val size: Int
        get() = list.size

    operator fun get(pos: Int) = list[pos]

    operator fun get(currencyName: String) = list.find { it.currency == currencyName }

    private val list: MutableList<Currency>

    init {

        val baseCurrency = rates.base

        list = rates
                .currencies
                .asSequence()
                .map { Currency(it, rates.ratesFor(it to baseCurrency) * initialValue) }
                .toMutableList()
    }

    private fun updateValues() {

        for (i in 1 until size)
            list[i].amount = base.amount * rates.ratesFor(list[i].currency to base.currency)

        observable.onNext(IntRange(1, size))
    }
}