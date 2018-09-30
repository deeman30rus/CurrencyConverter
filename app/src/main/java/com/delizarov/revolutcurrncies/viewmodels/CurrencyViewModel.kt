package com.delizarov.revolutcurrncies.viewmodels

import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import com.delizarov.revolutcurrncies.domain.Currency

class CurrencyViewModel(
        val currency: Currency,
        val fullName: String,
        val icon: Drawable,
        var amount: Float
)