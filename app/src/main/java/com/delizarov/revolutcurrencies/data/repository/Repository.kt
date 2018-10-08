package com.delizarov.revolutcurrencies.data.repository

import com.delizarov.revolutcurrencies.domain.ExchangeRates
import io.reactivex.Observable

interface Repository {

    val ratesObservable: Observable<ExchangeRates>
}