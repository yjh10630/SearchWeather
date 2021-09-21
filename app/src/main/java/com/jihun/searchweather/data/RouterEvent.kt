package com.jihun.searchweather.data


const val LAT = "lat"
const val LONG = "long"
const val CITY = "city"

data class RouterEvent (
    val type: Landing,
    var lat: Double = 0.0,
    var long: Double = 0.0,
    var city: String? = null
)

enum class Landing {
    MAIN,
    DETAIL
}