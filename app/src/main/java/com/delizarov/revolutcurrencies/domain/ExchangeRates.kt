package com.delizarov.revolutcurrencies.domain

class ExchangeRates(
        var base: Currency1 = "",
        rates: Map<Currency1, Float> = HashMap()
) {

    private val rates: MutableMap<Currency1, Float> = HashMap()

    init {
        this.rates[this.base] = 1f
        this.rates.putAll(rates)
    }

    val currencies: List<Currency1>
        get() {
            return if (base.isEmpty()) emptyList() else listOf(base).union(rates.keys).toList()
        }

    fun ratesFor(pair: Pair<Currency1, Currency1>): Float {

        val fromRate = rates[pair.first]
        val toRate = rates[pair.second]

        return fromRate!! / toRate!!
    }
}