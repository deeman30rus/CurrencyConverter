package com.delizarov.revolutcurrncies.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.delizarov.revolutcurrncies.App
import com.delizarov.revolutcurrncies.R
import com.delizarov.revolutcurrncies.data.network.RevolutApi

class MainActivity : AppCompatActivity() {

    private val api: RevolutApi by lazy { (application as App).api }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}
