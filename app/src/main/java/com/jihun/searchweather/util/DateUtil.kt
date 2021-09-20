package com.jihun.searchweather.util

import java.text.SimpleDateFormat
import java.util.*

// 유닉스 변환
fun Int.convertToDate(timeZone: String?): String? {
    val timestamp = this.toLong()
    val date = Date(timestamp * 1000L)
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone(timeZone)
    return sdf.format(date)
}