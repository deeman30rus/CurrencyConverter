package com.delizarov.revolutcurrncies.data.repository

import com.delizarov.revolutcurrncies.domain.ExchangeRates
import io.reactivex.Observable

interface Repository {

    val ratesObservable: Observable<ExchangeRates>
}