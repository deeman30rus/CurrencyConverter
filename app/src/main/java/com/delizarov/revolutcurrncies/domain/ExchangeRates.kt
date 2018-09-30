package com.delizarov.revolutcurrncies.domain

class ExchangeRates {

    var base: Currency = ""
    private val rates: MutableMap<Currency, Float> = HashMap()

    val currencies: List<Currency>
        get() {
            return if (base.isEmpty()) emptyList() else listOf(base).union(rates.keys).toList()
        }

    fun updateRates(base: Currency, rates: Map<Currency, Float>) {
        this.base = base

        this.rates[this.base] = 1f
        this.rates.putAll(rates)
    }

    fun ratesFor(pair: Pair<Currency, Currency>): Float {

        val fromRate = rates[pair.first]
        val toRate = rates[pair.second]

        return fromRate!! / toRate!!
    }
}