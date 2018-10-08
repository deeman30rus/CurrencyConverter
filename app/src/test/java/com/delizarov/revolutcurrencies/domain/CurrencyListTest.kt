package com.delizarov.revolutcurrencies.domain

import com.delizarov.revolutcurrencies.*
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CurrencyListTest {

    private lateinit var exchangeRates1: ExchangeRates
    private lateinit var exchangeRates2: ExchangeRates
    private lateinit var emptyRates: ExchangeRates

    @Before
    fun `prepare dataset`() {

        val rates1 = mapOf(
                RUB to RUB_TO_EUR_RATE_1,
                USD to USD_TO_EUR_RATE_1
        )

        val rates2 = mapOf(
                RUB to RUB_TO_EUR_RATE_1,
                USD to USD_TO_EUR_RATE_1
        )

        exchangeRates1 = ExchangeRates(EUR, rates1)
        exchangeRates2 = ExchangeRates(EUR, rates2)
        emptyRates = ExchangeRates()
    }

    @Test
    fun `currency list creation test`() {

        val initialValue = 100f
        val currencies = CurrencyList(exchangeRates1)

        val expectedCurrency = Currency(EUR, initialValue)

        assertEquals(3, currencies.size)
        assertEquals(expectedCurrency.currency, currencies.baseCurrency)
        assertEquals(expectedCurrency.amount, currencies.baseAmount)

    }

    @Test
    fun `change base amount test`() {

        val initialValue = 100f
        val scaleFactor = 2
        val currencies = CurrencyList(exchangeRates1)

        val usdCurrencyAmount = currencies[USD]!!.amount
        val rubCurrencyAmount = currencies[RUB]!!.amount

        currencies.baseAmount = initialValue * scaleFactor

        assertEquals(usdCurrencyAmount * scaleFactor, currencies[USD]!!.amount)
        assertEquals(rubCurrencyAmount * scaleFactor, currencies[RUB]!!.amount)
    }

    @Test
    fun `change base currency test`() {

        val newBaseCurrency = RUB
        val currencies = CurrencyList(exchangeRates1)

        val expectedAmount = currencies[newBaseCurrency]!!.amount

        currencies.baseCurrency = newBaseCurrency

        assertEquals(expectedAmount, currencies.baseAmount)
        assertEquals(newBaseCurrency, currencies.baseCurrency)
    }

    @Test
    fun `change exchange rates test`() {

        val initialValue = 100f
        val currencies = CurrencyList(exchangeRates1, initialValue)

        currencies.rates = exchangeRates2

        val rubAmountAfter = currencies[RUB]!!.amount
        val expectedAfter = initialValue * exchangeRates2.ratesFor(RUB to EUR)

        assertEquals(expectedAfter, rubAmountAfter)
    }
}