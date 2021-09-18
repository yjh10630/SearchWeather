package com.jihun.searchweather.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

infix fun Any?.hideKeyboard(currentFocus: View) {
    if (currentFocus is EditText) {
        currentFocus.apply {
            val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
            clearFocus()
        }
    }
}