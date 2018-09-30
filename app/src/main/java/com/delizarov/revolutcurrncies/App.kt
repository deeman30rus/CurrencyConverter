package com.delizarov.revolutcurrncies

import android.app.Application
import com.delizarov.revolutcurrncies.data.network.RevolutApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class App : Application() {

    val api: RevolutApi by lazy {
        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        retrofit.create(RevolutApi::class.java)
    }
}