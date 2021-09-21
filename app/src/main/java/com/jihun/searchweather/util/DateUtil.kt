package com.jihun.searchweather.util

import java.text.SimpleDateFormat
import java.util.*

const val DATE_FORMAT_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss"
const val DATE_FORMAT_HHMM = "HH:mm"
const val DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd"

// 유닉스 변환
fun Int.convertToDate(timeZone: String?, format: String): String? {
    val timestamp = this.toLong()
    val date = Date(timestamp * 1000L)
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone(timeZone)
    return sdf.format(date)
}