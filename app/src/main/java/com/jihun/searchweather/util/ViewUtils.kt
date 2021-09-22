package com.jihun.searchweather.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

inline val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

inline val Float.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

infix fun Any?.hideKeyboard(currentFocus: View) {
    if (currentFocus is EditText) {
        currentFocus.apply {
            val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
            clearFocus()
        }
    }
}

fun getSpannedColorText(origin: String, changed: String, color: Int, bold: Boolean = false): Spannable {
    val sb = SpannableStringBuilder(origin)
    val pair = getChangedIndex(origin, changed)

    sb.setSpan(
        ForegroundColorSpan(color),
        pair.first,
        pair.second,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    if (bold) {
        sb.setSpan(
            StyleSpan(Typeface.BOLD),
            pair.first,
            pair.second,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return sb
}

fun getSpannedSizeText(
    origin: String,
    changed: String,
    sizeDp: Int,
    bold: Boolean = false
): Spannable {

    val sb = SpannableStringBuilder(origin)
    val pair = getChangedIndex(origin, changed)

    sb.setSpan(
        AbsoluteSizeSpan(sizeDp, true),
        pair.first,
        pair.second,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    if (bold) {
        sb.setSpan(
            StyleSpan(Typeface.BOLD),
            pair.first,
            pair.second,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return sb
}

private fun getChangedIndex(origin: String, changed: String): Pair<Int, Int> {
    if (origin.isEmpty()) return Pair(0, 0)

    var start = origin.indexOf(changed, 0)
    var end = start + changed.length
    if (start == -1) {
        start = 0
        end = origin.length
    }
    return Pair(start, end)
}