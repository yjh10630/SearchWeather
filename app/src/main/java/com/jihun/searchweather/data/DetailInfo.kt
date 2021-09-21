package com.jihun.searchweather.data

import com.google.gson.annotations.SerializedName
import com.jihun.searchweather.ViewType

data class DetailModule(
    var type: ViewType = ViewType.EMPTY,
    var data: Any? = null,
)

// 현재 날씨 Model
data class DetailCurrentWeather(
    val temp: String?,
    val description: String?,
    val iconUrl: String?,
    val humidity: String?,
    val skyStatus: String?,
    val sunrise: String?,
    val sunset: String?,
    val wind: String?
)

// 시간별 날씨 Model
data class DetailHourlyWeather(
    val tabList: List<String>?,
    var selectedIndex: Int = 0,
    var tempList: List<TimeValueData>? = null,
    var humList: List<TimeValueData>? = null,
    var windList: List<TimeValueData>? = null,
    var popList: List<TimeValueData>? = null
)

data class TimeValueData(
    val time: String?,
    val value: String?
)

// 일별 날씨 ( 주간 예보 ) Model
data class DetailDailyWeather(
    val date: String?,
    val iconUrl: String?,
    val tempMin: String?,
    val tempMax: String?,
    val pop: String?
)


/**
 * Response Data class
 */
data class DetailInfo(
    @SerializedName("current") var current: Current? = null, // 현재 날씨 데이터
    @SerializedName("daily") var daily: List<Daily>? = null, // 일별 일기예보
    @SerializedName("hourly") var hourly: List<Hourly>? = null, // 시간별 기상 데이터
    @SerializedName("lat") var lat: Double,
    @SerializedName("lon") var lon: Double,
    @SerializedName("timezone") var timezone: String? = null,
    @SerializedName("timezone_offset") var timezoneOffset: Int
)

data class Current(
    @SerializedName("clouds") var clouds: Int,      // 강수확률 %
    @SerializedName("dew_point") var dewPoint: Double,
    @SerializedName("dt") var dt: Int,
    @SerializedName("feels_like") var feelsLike: Double,
    @SerializedName("humidity") var humidity: Int,  // 습도 %
    @SerializedName("pressure") var pressure: Int,
    @SerializedName("sunrise") var sunrise: Int,    // 일출 시간 유닉스
    @SerializedName("sunset") var sunset: Int,      // 일몰 시간 유닉스
    @SerializedName("temp") var temp: Double,       // 온도
    @SerializedName("uvi") var uvi: Double,         // 자외선 지수
    @SerializedName("visibility") var visibility: Int,
    @SerializedName("weather") var weather: List<Weather>? = null,
    @SerializedName("wind_deg") var windDeg: Int,           // 풍향
    @SerializedName("wind_gust") var windGust: Double,      // 돌풍
    @SerializedName("wind_speed") var windSpeed: Double     // 풍속
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