package com.jihun.searchweather.data


import com.google.gson.annotations.SerializedName

data class DetailInfo(
    @SerializedName("current") var current: Current? = null,
    @SerializedName("daily") var daily: List<Daily>? = null,
    @SerializedName("hourly") var hourly: List<Hourly>? = null,
    @SerializedName("lat") var lat: Double,
    @SerializedName("lon") var lon: Double,
    @SerializedName("timezone") var timezone: String? = null,
    @SerializedName("timezone_offset") var timezoneOffset: Int
)

data class Current(
    @SerializedName("clouds") var clouds: Int,
    @SerializedName("dew_point") var dewPoint: Double,
    @SerializedName("dt") var dt: Int,
    @SerializedName("feels_like") var feelsLike: Double,
    @SerializedName("humidity") var humidity: Int,
    @SerializedName("pressure") var pressure: Int,
    @SerializedName("sunrise") var sunrise: Int,
    @SerializedName("sunset") var sunset: Int,
    @SerializedName("temp") var temp: Double,
    @SerializedName("uvi") var uvi: Double,
    @SerializedName("visibility") var visibility: Int,
    @SerializedName("weather") var weather: List<Weather>? = null,
    @SerializedName("wind_deg") var windDeg: Int,
    @SerializedName("wind_gust") var windGust: Double,
    @SerializedName("wind_speed") var windSpeed: Double
)

data class Weather(
    @SerializedName("description") var description: String? = null,
    @SerializedName("icon") var icon: String? = null,
    @SerializedName("id") var id: Int,
    @SerializedName("main") var main: String? = null
)

data class Daily(
    @SerializedName("clouds") var clouds: Int,
    @SerializedName("dew_point") var dewPoint: Double,
    @SerializedName("dt") var dt: Int,
    @SerializedName("feels_like") var feelsLike: FeelsLike? = null,
    @SerializedName("humidity") var humidity: Int,
    @SerializedName("moon_phase") var moonPhase: Double,
    @SerializedName("moonrise") var moonrise: Int,
    @SerializedName("moonset") var moonset: Int,
    @SerializedName("pop") var pop: Double,
    @SerializedName("pressure") var pressure: Int,
    @SerializedName("rain") var rain: Double,
    @SerializedName("sunrise") var sunrise: Int,
    @SerializedName("sunset") var sunset: Int,
    @SerializedName("temp") var temp: Temp? = null,
    @SerializedName("uvi") var uvi: Double,
    @SerializedName("weather") var weather: List<Weather>? = null,
    @SerializedName("wind_deg") var windDeg: Int,
    @SerializedName("wind_gust") var windGust: Double,
    @SerializedName("wind_speed") var windSpeed: Double
)

data class FeelsLike(
    @SerializedName("day") var day: Double,
    @SerializedName("eve") var eve: Double,
    @SerializedName("morn") var morn: Double,
    @SerializedName("night") var night: Double
)

data class Hourly(
    @SerializedName("clouds") var clouds: Int,
    @SerializedName("dew_point") var dewPoint: Double,
    @SerializedName("dt") var dt: Int,
    @SerializedName("feels_like") var feelsLike: Double,
    @SerializedName("humidity") var humidity: Int,
    @SerializedName("pop") var pop: Double,
    @SerializedName("pressure") var pressure: Int,
    @SerializedName("rain") var rain: Rain? = null,
    @SerializedName("temp") var temp: Double,
    @SerializedName("uvi") var uvi: Double,
    @SerializedName("visibility") var visibility: Int,
    @SerializedName("weather") var weather: List<Weather>? = null,
    @SerializedName("wind_deg") var windDeg: Int,
    @SerializedName("wind_gust") var windGust: Double,
    @SerializedName("wind_speed") var windSpeed: Double
)

data class Temp(
    @SerializedName("day") var day: Double,
    @SerializedName("eve") var eve: Double,
    @SerializedName("max") var max: Double,
    @SerializedName("min") var min: Double,
    @SerializedName("morn") var morn: Double,
    @SerializedName("night") var night: Double
)

data class Rain(
    @SerializedName("1h") var h: Double
)