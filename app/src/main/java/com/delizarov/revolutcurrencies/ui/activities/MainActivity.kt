package com.delizarov.revolutcurrencies.ui.activities

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.delizarov.revolutcurrencies.App
import com.delizarov.revolutcurrencies.R
import com.delizarov.revolutcurrencies.data.repository.Repository
import com.delizarov.revolutcurrencies.domain.CurrencyList
import com.delizarov.revolutcurrencies.domain.ExchangeRates
import com.delizarov.revolutcurrencies.extentions.bind
import com.delizarov.revolutcurrencies.ui.adapters.CurrencyListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable


class MainActivity : AppCompatActivity() {

    private val repository: Repository by lazy { (application as App).repo }

    private val currencies: RecyclerView by bind(R.id.currencies)
    private val dnaMessage: TextView by bind(R.id.dna_message)

    private var ratesDisposable: Disposable? = null

    private val adapter = CurrencyListAdapter(this)

    private var listState: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currencies.itemAnimator = null
        currencies.adapter = adapter
        currencies.setOnTouchListener { v, _ ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    override fun onResume() {
        super.onResume()

        if (listState != null)
            currencies.layoutManager.onRestoreInstanceState(listState)

        ratesDisposable?.dispose()
        ratesDisposable = repository.ratesObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { exchangeRates ->
                    if (exchangeRates.currencies.isEmpty()) {
                        showDataNotAvailable()
                    } else {
                        showCurrenciesList(exchangeRates)
                    }
                }
    }

    override fun onPause() {
        super.onPause()

        ratesDisposable?.dispose()
    }

    private fun showCurrenciesList(exchangeRates: ExchangeRates) {

        dnaMessage.visibility = View.GONE
        currencies.visibility = View.VISIBLE

        if (adapter.itemCount == 0) {
            adapter.currencies = CurrencyList(exchangeRates)
        } else {
            adapter.updateExchangeRates(exchangeRates)
        }

        adapter.updateExchangeRates(exchangeRates)
    }

    private fun showDataNotAvailable() {

        dnaMessage.visibility = View.VISIBLE
        currencies.visibility = View.GONE
    }
}
