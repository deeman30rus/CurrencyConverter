package com.delizarov.revolutcurrencies.extentions

import android.content.Context
import com.delizarov.revolutcurrencies.domain.Currency
import com.delizarov.revolutcurrencies.viewmodels.CurrencyViewModel


fun Currency.toViewModel(ctx: Context, isResponder: Boolean) = CurrencyViewModel(
        currency,
        ctx.getFullNameForCurrency(currency),
        ctx.getIconResForCurrency(currency),
        amount,
        isResponder
)
