package com.delizarov.revolutcurrncies

import android.app.Application
import com.delizarov.revolutcurrncies.data.network.RevolutApi
import retrofit2.Retrofit


class App : Application() {

    val api: RevolutApi by lazy {
        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .build()


        retrofit.create(RevolutApi::class.java)
    }
}