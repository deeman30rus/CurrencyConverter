package com.delizarov.revolutcurrencies.viewmodels

import android.graphics.drawable.Drawable
import com.delizarov.revolutcurrencies.domain.Currency1

class CurrencyViewModel(
        val currency1: Currency1,
        val fullName: String,
        val icon: Drawable,
        var amount: Float,
        var isResponder: Boolean
)