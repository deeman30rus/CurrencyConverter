package com.delizarov.revolutcurrncies.extentions

import android.content.Context
import com.delizarov.revolutcurrncies.domain.Currency
import com.delizarov.revolutcurrncies.viewmodels.CurrencyViewModel


fun Currency.toViewModel(ctx: Context, isResponder: Boolean) = CurrencyViewModel(
        currency,
        ctx.getFullNameForCurrency(currency),
        ctx.getIconResForCurrency(currency),
        amount,
        isResponder
)
