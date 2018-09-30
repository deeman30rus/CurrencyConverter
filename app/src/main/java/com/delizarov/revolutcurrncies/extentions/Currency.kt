package com.delizarov.revolutcurrncies.extentions

import android.content.Context
import com.delizarov.revolutcurrncies.domain.Currency

fun Currency.toViewModel(ctx: Context) = com.delizarov.revolutcurrncies.viewmodels.CurrencyViewModel(
        this,
        ctx.getFullNameForCurrency(this),
        ctx.getIconResForCurrency(this),
        0f
)
