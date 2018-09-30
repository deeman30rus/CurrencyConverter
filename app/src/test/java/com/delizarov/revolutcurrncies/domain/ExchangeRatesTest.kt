package com.delizarov.revolutcurrncies.domain

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test

const val EPS = 0.00001

class ExchangeRatesTest {

    @Test
    fun `create empty object test`() {

        val exchangeRates = ExchangeRates()

        assertEquals(emptySet<Currency>(), exchangeRates.currencies)
    }

    @Test
    fun `exchange rate test`() {

        val rubToEURRate = 76.39f
        val usdToUSDRate = 1.16f

        val rates = mapOf(
                "RUB" to rubToEURRate,
                "USD" to usdToUSDRate
        )

        val exchangeRates = ExchangeRates()
        exchangeRates.updateRates("EUR", rates)

        assertFloatingEquals((rubToEURRate / usdToUSDRate), exchangeRates.ratesFor("RUB" to "USD"))
    }

    @Test
    fun `same exchange test`() {

        val rubToEURRate = 76.39f

        val rates = mapOf("RUB" to rubToEURRate)

        val exchangeRates = ExchangeRates()
        exchangeRates.updateRates("EUR", rates)

        assertFloatingEquals(1f, exchangeRates.ratesFor("EUR" to "EUR"))
        assertFloatingEquals(1f, exchangeRates.ratesFor("RUB" to "RUB"))
    }

    private fun assertFloatingEquals(num1: Float, num2: Float) = assertTrue(num1 - num2 < EPS)
}