package com.jihun.searchweather.data

import com.google.gson.annotations.SerializedName
import com.jihun.searchweather.ViewType

data class MainModule(
    var type: ViewType = ViewType.EMPTY,
    var data: Any? = null,
)

data class CityHeader(
    val name: String?,
    val count: Int
)

data class CityInfo (
    @SerializedName("id") val id: Int,
    @SerializedName("country") val country: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("coord") val coord: Coord?
) {
    data class Coord(
        @SerializedName("lat") val lat: Double,
        @SerializedName("lon") val lon: Double,
    )
}