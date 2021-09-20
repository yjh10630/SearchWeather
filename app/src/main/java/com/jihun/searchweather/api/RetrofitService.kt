package com.jihun.searchweather.api

import com.jihun.searchweather.data.DetailInfo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("onecall?appid=5c7059402c6bbc3a50ff6775868e1976&units=metric&lang=kr&exclude=alerts,minutely")
    fun requestGetOneCallWeatherData(
        @Query("lat", encoded = true) lat: Double,
        @Query("lon", encoded = true) lon: Double): Observable<DetailInfo>
}