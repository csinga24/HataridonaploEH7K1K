package hu.bme.aut.android.hataridonaploeh7k1k.extension

import android.widget.EditText

fun EditText.validateNonEmpty(): Boolean {
    if (text.isEmpty()) {
        error = "Required"
        return false
    }
    return true
}