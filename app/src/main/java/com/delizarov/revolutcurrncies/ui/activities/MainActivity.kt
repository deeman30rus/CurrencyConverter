package com.delizarov.revolutcurrncies.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.delizarov.revolutcurrncies.App
import com.delizarov.revolutcurrncies.R
import com.delizarov.revolutcurrncies.data.repository.Repository
import com.delizarov.revolutcurrncies.data.repository.impl.RepositoryImpl
import com.delizarov.revolutcurrncies.domain.CurrencyList
import com.delizarov.revolutcurrncies.domain.ExchangeRates
import com.delizarov.revolutcurrncies.extentions.bind
import com.delizarov.revolutcurrncies.ui.adapters.CurrencyListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable


class MainActivity : AppCompatActivity() {

    private val repository: Repository by lazy { RepositoryImpl((application as App).api) }

    private val currencies: RecyclerView by bind(R.id.currencies)
    private val dnaMessage: TextView by bind(R.id.dna_message)

    private var ratesDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currencies.itemAnimator = null
    }

    override fun onResume() {
        super.onResume()

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

        var adapter = (currencies.adapter as? CurrencyListAdapter)

        if (adapter == null) {

            adapter = CurrencyListAdapter(CurrencyList(exchangeRates), this@MainActivity)

            currencies.adapter = adapter
        }

        adapter.updateExchangeRates(exchangeRates)
    }

    private fun showDataNotAvailable() {

        dnaMessage.visibility = View.VISIBLE
        currencies.visibility = View.GONE
    }
}
