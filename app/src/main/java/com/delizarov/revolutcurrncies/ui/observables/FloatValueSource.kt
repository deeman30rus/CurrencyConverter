package com.delizarov.revolutcurrncies.ui.observables

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.reactivex.subjects.PublishSubject

private const val FLOAT_PATTERN = "\\d+\\.?\\d{0,2}"

class FloatValueSource {

    private val regex = FLOAT_PATTERN.toRegex()

    var source: EditText? = null
        set(value) {

            field?.removeTextChangedListener(watcher)

            field = value

            field?.addTextChangedListener(watcher)
        }

    private val watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

            val str = s?.toString()

            if (str != null && !regex.matches(str)) {

                val dotPos = str.indexOf('.')

                s.delete(dotPos + 2, s.length - 1)
            }

            subject.onNext(s.toString().toFloatOrNull() ?: 0f)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    }

    private val subject: PublishSubject<Float> = PublishSubject.create()
    val observable = subject
}