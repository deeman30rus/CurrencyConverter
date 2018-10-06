package com.delizarov.revolutcurrncies.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.delizarov.revolutcurrncies.R
import com.delizarov.revolutcurrncies.domain.CurrencyList
import com.delizarov.revolutcurrncies.domain.ExchangeRates
import com.delizarov.revolutcurrncies.extentions.bind
import com.delizarov.revolutcurrncies.extentions.onClick
import com.delizarov.revolutcurrncies.extentions.toViewModel
import com.delizarov.revolutcurrncies.ui.observables.FloatValueSource
import com.delizarov.revolutcurrncies.viewmodels.CurrencyViewModel
import io.reactivex.disposables.Disposable

class CurrencyListAdapter(
        private val context: Context
) : RecyclerView.Adapter<CurrencyViewHolder>() {

    private val responderValueSource = FloatValueSource()

    private var responseValueDisposable: Disposable? = null
    private var currenciesDisposable: Disposable? = null

    var currencies: CurrencyList = CurrencyList(ExchangeRates())
        set(value) {

            unsubscribe()

            field = value

            currenciesDisposable?.dispose()
            currenciesDisposable = field
                    .observable
                    .subscribe { range ->
                        notifyItemRangeChanged(range.start, range.endInclusive)
                    }

            subscribe()

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {

        val inflater = LayoutInflater.from(context)

        return CurrencyViewHolder(inflater.inflate(R.layout.viewholder_currency, parent, false))
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {

        val currency = currencies[position]
        val isResponder = currency.currency == currencies.baseCurrency
        val vm = currency.toViewModel(context, isResponder)

        with(holder) {

            render(vm)

            if (!isResponder) onClick {

                unsubscribe()
                currencies.baseCurrency = currency.currency

            } else {
                if (responderValueSource.source == null) {
                    responderValueSource.source = amount
                }
            }
        }
    }

    override fun onViewRecycled(holder: CurrencyViewHolder) {
        super.onViewRecycled(holder)

        if (holder.amount == responderValueSource.source) {
            unsubscribe()
        }
    }

    override fun getItemCount() = currencies.size

    fun updateExchangeRates(rates: ExchangeRates) {

        currencies.rates = rates
    }

    private fun subscribe() {

        responseValueDisposable = responderValueSource
                .observable
                .subscribe { value ->
                    currencies.baseAmount = value
                }
    }

    private fun unsubscribe() {

        responseValueDisposable?.dispose()
        responderValueSource.source = null
    }
}

class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val icon: ImageView by bind(R.id.flag_icon)
    private val shortName: TextView by bind(R.id.short_name)
    private val fullName: TextView by bind(R.id.full_name)
    val amount: EditText by bind(R.id.amount)

    fun render(viewModel: CurrencyViewModel) {

        icon.setImageDrawable(viewModel.icon)

        shortName.text = viewModel.currency1
        fullName.text = viewModel.fullName
        amount.setText(String.format("%.2f", viewModel.amount))

        amount.isEnabled = viewModel.isResponder
    }
}