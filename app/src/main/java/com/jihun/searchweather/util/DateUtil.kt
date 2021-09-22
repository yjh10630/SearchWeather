package com.jihun.searchweather.util

import java.text.SimpleDateFormat
import java.util.*

const val DATE_FORMAT_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss"
const val DATE_FORMAT_HHMM = "HH:mm"
const val DATE_FORMAT_HH = "HH시"
const val DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd"
const val DATE_FORMAT_MMDOTDD = "MM.dd"
const val DATE_FORMAT_YYYYMMDD000000 = "yyyy-MM-dd 00:00:00"

// 유닉스 변환
fun Int.convertToDate(timeZone: String?, format: String): String? {
    val timestamp = this.toLong()
    val date = Date(timestamp * 1000L)
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone(timeZone)
    return sdf.format(date)
}

fun String.getDiffDate(timeZone: String?, format: String?): Int {
    val sdf = SimpleDateFormat(format, Locale.getDefault()).apply { this.timeZone = TimeZone.getTimeZone(timeZone) }
    val inputDate = sdf.parse(this)
    val today = Calendar.getInstance().apply {
        setTimeZone(TimeZone.getTimeZone(timeZone))
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0) }
    return ((inputDate.time - today.time.time) / (60 * 60 * 24 * 1000)).toInt()
}

fun String.getDateDay(timeZone: String?, format: String): String {
    val sdf = SimpleDateFormat(format, Locale.getDefault()).apply { this.timeZone = TimeZone.getTimeZone(timeZone) }
    val ndate = sdf.parse(this)
    val cal = Calendar.getInstance().apply { time = ndate }
    return when(cal.get(Calendar.DAY_OF_WEEK)) {
        1 -> "일"
        2 -> "월"
        3 -> "화"
        4 -> "수"
        5 -> "목"
        6 -> "금"
        7 -> "토"
        else -> ""
    }
}