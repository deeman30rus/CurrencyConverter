package com.delizarov.revolutcurrncies.domain

import com.delizarov.revolutcurrncies.*
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ExchangeRatesTest {

    private lateinit var exchangeRates: ExchangeRates
    private lateinit var emptyRates: ExchangeRates

    @Before
    fun `prepare dataset`() {

        val rates = mapOf(
                RUB to RUB_TO_EUR_RATE_1,
                USD to USD_TO_EUR_RATE_1
        )

        exchangeRates = ExchangeRates(EUR, rates)
        emptyRates = ExchangeRates()
    }

    @Test
    fun `create empty object test`() {

        assertEquals(emptyList<Currency1>(), emptyRates.currencies)
    }

    @Test
    fun `get currencies test`() {

        val expected = setOf(
                EUR,
                RUB,
                USD
        )

        assertEquals(expected, exchangeRates.currencies.toSet())
    }

    @Test
    fun `exchange rate test`() {

        val expected = (RUB_TO_EUR_RATE_1 / USD_TO_EUR_RATE_1)

        assertEquals(expected, exchangeRates.ratesFor(RUB to USD))
    }

    @Test
    fun `same exchange test`() {

        val expected = 1f

        assertEquals(expected, exchangeRates.ratesFor(EUR to EUR))
        assertEquals(expected, exchangeRates.ratesFor(RUB to RUB))
    }
}