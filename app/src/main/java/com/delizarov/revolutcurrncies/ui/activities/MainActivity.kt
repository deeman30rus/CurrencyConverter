package com.delizarov.revolutcurrncies.ui.activities

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.delizarov.revolutcurrncies.App
import com.delizarov.revolutcurrncies.R
import com.delizarov.revolutcurrncies.data.repository.Repository
import com.delizarov.revolutcurrncies.data.repository.impl.RepositoryImpl
import com.delizarov.revolutcurrncies.domain.Currency
import com.delizarov.revolutcurrncies.domain.ExchangeRates
import com.delizarov.revolutcurrncies.extentions.bind
import com.delizarov.revolutcurrncies.extentions.onClick
import com.delizarov.revolutcurrncies.extentions.toViewModel
import com.delizarov.revolutcurrncies.viewmodels.CurrencyViewModel
import io.reactivex.android.schedulers.AndroidSchedulers


private const val INITIAL_VALUE = 100f

class MainActivity : AppCompatActivity() {

    private val repository: Repository by lazy { RepositoryImpl((application as App).api) }

    private val currencies: RecyclerView by bind(R.id.currencies)
    private val dnaMessage: TextView by bind(R.id.dna_message)


    private val adapter = CurrencyListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository.ratesObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { exchangeRates ->
                    if (exchangeRates.currencies.isEmpty()) {
                        showDataNotAvailable()
                    } else {
                        showCurrenciesList(exchangeRates)
                    }
                }

        currencies.itemAnimator = null

        currencies.adapter = adapter
    }

    private fun showCurrenciesList(exchangeRates: ExchangeRates) {

        dnaMessage.visibility = View.GONE
        currencies.visibility = View.VISIBLE

        adapter.updateExchangeRates(exchangeRates)
    }

    private fun showDataNotAvailable() {

        dnaMessage.visibility = View.VISIBLE
        currencies.visibility = View.GONE
    }
}

private class CurrencyListAdapter(
        private val context: Context
) : RecyclerView.Adapter<CurrencyViewHolder>() {

    private var responderCurrency: Currency = ""
        set(value) {
            val responderVM = viewModels.find { it.currency == value }
            val position = viewModels.indexOf(responderVM)

            for (i in position downTo 1)
                viewModels[i] = viewModels[i - 1].also { viewModels[i - 1] = viewModels[i] }

            responderValue = viewModels[0].amount

            field = value

            notifyItemRangeChanged(0, position)
        }

    private var responderValue: Float = 0f

    private val viewModels = mutableListOf<CurrencyViewModel>()

    private lateinit var exchangeRates: ExchangeRates

    fun updateExchangeRates(rates: ExchangeRates) {

        exchangeRates = rates

        if (viewModels.isEmpty()) {

            viewModels.addAll(exchangeRates.currencies.map { it.toViewModel(context) })

            responderCurrency = exchangeRates.base
            responderValue = INITIAL_VALUE

            viewModels[0].amount = responderValue

            notifyDataSetChanged()
        } else {

            viewModels[0].amount = responderValue

            for (i in 1 until itemCount) {
                val vm = viewModels[i]
                vm.amount = (exchangeRates.ratesFor(vm.currency to responderCurrency) * responderValue)
            }

            notifyItemRangeChanged(1, itemCount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {

        val inflater = LayoutInflater.from(context)

        return CurrencyViewHolder(inflater.inflate(R.layout.viewholder_currency, parent, false))
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {

        val vm = viewModels[position]

        with(holder) {
            icon.setImageDrawable(vm.icon)
            shortName.text = vm.currency
            fullName.text = vm.fullName
            amount.setText(vm.amount.toString())

            val isFirstResponder = vm.currency == responderCurrency

            amount.isEnabled = isFirstResponder

            if (!isFirstResponder) {

                onClick {
                    responderCurrency = vm.currency
                }

                return
            }

            amount.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    responderValue = s?.toString()?.toFloatOrNull() ?: 0f

                    for (i in 1 until itemCount) {
                        val vm1 = viewModels[i]
                        vm1.amount = exchangeRates.ratesFor(responderCurrency to vm1.currency) * responderValue
                    }

                    itemView.post { notifyItemRangeChanged(1, itemCount) }
                }
            })
        }
    }

    override fun getItemCount() = viewModels.size
}

private class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val icon: ImageView by bind(R.id.flag_icon) //.setImageDrawable(vm.icon)
    val shortName: TextView by bind(R.id.short_name) //.text = vm.currency
    val fullName: TextView by bind(R.id.full_name) //.text = vm.fullName
    val amount: EditText by bind(R.id.amount) //vm //.amount.toString())
}
