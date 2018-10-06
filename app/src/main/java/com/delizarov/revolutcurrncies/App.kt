package com.delizarov.revolutcurrncies

import android.app.Application
import com.delizarov.revolutcurrncies.data.network.RevolutApi
import com.delizarov.revolutcurrncies.data.repository.Repository
import com.delizarov.revolutcurrncies.data.repository.impl.RepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class App : Application() {

    val repo: Repository by lazy { RepositoryImpl(api) }

    private val api: RevolutApi by lazy {
        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        retrofit.create(RevolutApi::class.java)
    }
}