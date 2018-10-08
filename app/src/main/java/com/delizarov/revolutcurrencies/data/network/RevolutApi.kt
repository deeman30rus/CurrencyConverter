package com.delizarov.revolutcurrencies.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RevolutApi {

    @GET("latest")
    fun getLatestRates(@Query("base") base: String = "EUR") : Call<RatesDTO>
}