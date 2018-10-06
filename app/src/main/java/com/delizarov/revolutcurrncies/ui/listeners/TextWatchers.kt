package com.delizarov.revolutcurrncies.ui.listeners

import android.text.Editable
import android.text.TextWatcher

class FloatTextWatcher : TextWatcher {

    var callback: ((Float) -> Unit)? = null

    override fun afterTextChanged(s: Editable?) { }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        callback?.invoke(s?.toString()?.toFloatOrNull() ?: 0f)
    }
}