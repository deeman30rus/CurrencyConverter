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
        private val api: RevolutApi
) : Repository {

    private val networkingThread = thread {
        while (true) {

            val dto = try {
                api.getLatestRates().execute().body()
            } catch (ex: IOException) {
                ex.printStackTrace()
                null
            }

            if (dto != null)
                exchangeRates.updateRates(dto.base, dto.rates)
            

            Thread.sleep(UPDATE_RATE_MILLIS)
        }
    }

    private val exchangeRates = ExchangeRates()

    init {

        networkingThread.start()
    }

    override val ratesObservable = Observable
            .interval(1000L, TimeUnit.MILLISECONDS)
            .timeInterval()
            .flatMap { _ -> Observable.just(exchangeRates) }

}



