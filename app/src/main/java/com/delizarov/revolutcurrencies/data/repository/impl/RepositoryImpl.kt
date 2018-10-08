package com.delizarov.revolutcurrencies.data.repository.impl

import com.delizarov.revolutcurrencies.data.network.RevolutApi
import com.delizarov.revolutcurrencies.data.repository.Repository
import com.delizarov.revolutcurrencies.domain.ExchangeRates
import io.reactivex.Observable
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.thread

private const val UPDATE_RATE_MILLIS = 1000L

class RepositoryImpl(
        private val api: RevolutApi,
        private val updateRate: Long = UPDATE_RATE_MILLIS
) : Repository {

    private val ratesRef = AtomicReference<ExchangeRates>()

    private val networkingThread = infiniteThread {

        val dto = try {
            api.getLatestRates().execute().body()
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }

        if (dto != null)
            ratesRef.set(ExchangeRates(dto.base, dto.rates))

        Thread.sleep(updateRate)
    }

    init {
        networkingThread.start()
    }

    override val ratesObservable = Observable
            .interval(updateRate, TimeUnit.MILLISECONDS)
            .timeInterval()
            .flatMap { _ -> Observable.just(ratesRef.get() ?: ExchangeRates()) }

}

private fun infiniteThread(block: () -> Unit) = thread(start = false) {

    while (true)
        block.invoke()
}