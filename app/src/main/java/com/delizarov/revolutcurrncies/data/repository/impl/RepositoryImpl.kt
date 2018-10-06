package com.delizarov.revolutcurrncies.data.repository.impl

import android.util.Log
import com.delizarov.revolutcurrncies.data.network.RevolutApi
import com.delizarov.revolutcurrncies.data.repository.Repository
import com.delizarov.revolutcurrncies.domain.ExchangeRates
import io.reactivex.Observable
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

private const val UPDATE_RATE_MILLIS = 1000L

class RepositoryImpl(
        private val api: RevolutApi,
        private val updateRate: Long = UPDATE_RATE_MILLIS
) : Repository {

    private val exchangeRatesBuf = SyncBuffer<ExchangeRates>()

    private val networkingThread = infiniteThread {

        val dto = try {
            api.getLatestRates().execute().body()
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }

        if (dto != null)
            exchangeRatesBuf.write(ExchangeRates(dto.base, dto.rates))

        Thread.sleep(updateRate)
    }

    init {
        networkingThread.start()
    }

    override val ratesObservable = Observable
            .interval(updateRate, TimeUnit.MILLISECONDS)
            .timeInterval()
            .flatMap { _ -> Observable.just(exchangeRatesBuf.read() ?: ExchangeRates()) }

    private inner class SyncBuffer<T> {

        private var obj: T? = null

        fun write(obj: T?) = synchronized(this) {

            this.obj = obj
        }

        fun read(): T? = synchronized(this) {
            obj
        }
    }
}

private fun infiniteThread(block: () -> Unit) = thread {

    while (true)
        block.invoke()
}