package com.jihun.searchweather.data


const val LAT = "lat"
const val LONG = "long"

data class RouterEvent (
    val type: Landing,
    var lat: Double = 0.0,
    var long: Double = 0.0
)

enum class Landing {
    MAIN,
    DETAIL
}