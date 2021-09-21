package com.jihun.searchweather.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jihun.searchweather.ViewType
import com.jihun.searchweather.api.RetrofitClient
import com.jihun.searchweather.data.*
import com.jihun.searchweather.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class DetailViewModel: ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    private val _detailLiveData: MutableLiveData<MutableList<DetailModule>> = MutableLiveData()
    val detailLiveData: LiveData<MutableList<DetailModule>>
        get() = _detailLiveData

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getWeatherData(lat: Double, long: Double) {
        RetrofitClient.getInstance().getService().requestGetOneCallWeatherData(lat, long)
            .subscribeOn(Schedulers.io())
            //.doOnNext(::setLogCheckData)
            .map(::createViewEntity)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _detailLiveData.value = it
            }, {
                Log.d("####", "Fail > ${it.message}")
            })
            .addTo(compositeDisposable)
    }

    private fun createViewEntity(data: DetailInfo): MutableList<DetailModule> {
        val modules = mutableListOf<DetailModule>()

        with(modules) {
            validateToAddCurrentWeather(data)
            validateToAddHourlyWeather(data)
            validateToAddDailyWeather(data)
        }

        return modules
    }

    private fun MutableList<DetailModule>.validateToAddCurrentWeather(data: DetailInfo) {
        val current = data.current ?: return
        val weather = current.weather?.getOrNull(0)

        val currentData = DetailCurrentWeather(
            temp = "${current.temp.toInt()}°",
            description = weather?.description ?: "",
            iconUrl = "http://openweathermap.org/img/wn/${weather?.icon ?: ""}@2x.png",
            skyStatus = weather?.main ?: "",
            humidity = "${current.humidity}%",
            sunrise = data.current?.sunrise?.convertToDate(data.timezone, DATE_FORMAT_HHMM),
            sunset = data.current?.sunset?.convertToDate(data.timezone, DATE_FORMAT_HHMM),
            wind = "(${WindType.values().getOrNull((((data.current?.windDeg ?: 0) + 22.5 * 0.5) / 22.5).toInt())?.codeNm})${data.current?.windSpeed}m/s"
        )
        add(DetailModule(type = ViewType.DETAIL_CURRENT_WEATHER, data = currentData))
    }

    private fun MutableList<DetailModule>.validateToAddHourlyWeather(data: DetailInfo) {
        val hourlyList = data.hourly
        if (hourlyList.isNullOrEmpty()) return

        val tempList = mutableListOf<TimeValueData>().apply {
            hourlyList.forEach {
                add(TimeValueData(
                    time = it.dt.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDDHHMMSS),
                    value = "${it.temp.toInt()}°"
                ))
            }
        }

        val humList = mutableListOf<TimeValueData>().apply {
            hourlyList.forEach {
                add(TimeValueData(
                    time = it.dt.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDDHHMMSS),
                    value = "${it.humidity}%"
                ))
            }
        }

        val windList = mutableListOf<TimeValueData>().apply {
            hourlyList.forEach {
                add(TimeValueData(
                    time = it.dt.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDDHHMMSS),
                    value = "(${WindType.values().getOrNull((((it.windDeg) + 22.5 * 0.5) / 22.5).toInt())?.codeNm})${it.windSpeed}m/s"
                ))
            }
        }

        val popList = mutableListOf<TimeValueData>().apply {
            hourlyList.forEach {
                add(TimeValueData(
                    time = it.dt.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDDHHMMSS),
                    value = "${it.pop.toInt()}%"
                ))
            }
        }

        val tabList = mutableListOf<String>().apply {
            if (!tempList.isNullOrEmpty()) this.add("온도")
            if (!humList.isNullOrEmpty()) this.add("습도")
            if (!windList.isNullOrEmpty()) this.add("바람")
            if (!popList.isNullOrEmpty()) this.add("강수")
        }

        val hourlyData = DetailHourlyWeather(
            tabList = tabList,
            tempList = tempList,
            humList = humList,
            windList = windList,
            popList = popList
        )

        add(DetailModule(type = ViewType.DETAIL_HOURLY_WEATHER_TAB, data = hourlyData))
        add(DetailModule(type = ViewType.DETAIL_HOURLY_WEATHER_CONTENT, data = tempList))

    }

    private fun MutableList<DetailModule>.validateToAddDailyWeather(data: DetailInfo) {
        val daily = data.daily
        if (daily.isNullOrEmpty()) return

        daily.forEach {
            val dailyData = DetailDailyWeather(
                date = it.dt.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDD),
                iconUrl = "http://openweathermap.org/img/wn/${it.weather?.getOrNull(0)?.icon ?: ""}@2x.png",
                tempMin = "${it.temp?.min?.toInt()}°",
                tempMax = "${it.temp?.max?.toInt()}°",
                pop = "${it.pop.toInt()}%"
            )
            add(DetailModule(type = ViewType.DETAIL_DAILY_WEATHER, data = dailyData))
        }
    }

    /**
     * 데이터 확인용
     */
    private fun setLogCheckData(data: DetailInfo) {
        Log.d("####", "--------- current Weather ---------")
        Log.d("####", "\n" +
                "온도 : ${data.current?.temp}\n" +
                "상태 : ${data.current?.weather?.getOrNull(0)?.description}\n" +
                "아이콘 : ${data.current?.weather?.getOrNull(0)?.icon}\n" +
                "습도 : ${data.current?.humidity}\n" +
                "강수 확률 : ${data.current?.clouds}\n" +
                "하늘 상태 : ${data.current?.weather?.getOrNull(0)?.main}\n" +
                "일출 : ${data.current?.sunrise?.convertToDate(data.timezone, DATE_FORMAT_HHMM)}\n" +
                "일몰 : ${data.current?.sunset?.convertToDate(data.timezone, DATE_FORMAT_HHMM)}\n" +
                "바람 : (${WindType.values().getOrNull((((data.current?.windDeg ?: 0) + 22.5 * 0.5) / 22.5).toInt())?.codeNm})${data.current?.windSpeed}m/s"
        )

        Log.d("####", "\n--------- Hourly Weather(온도) ---------\n")
        data.hourly?.forEach { hourly ->
            Log.d("####", "시간 : ${hourly.dt.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDDHHMMSS)} 온도 : ${hourly.temp}")
        }

        Log.d("####", "\n--------- Hourly Weather(습도) ---------\n")
        data.hourly?.forEach { hourly ->
            Log.d("####", "시간 : ${hourly.dt.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDDHHMMSS)} 습도 : ${hourly.humidity}")
        }

        Log.d("####", "\n--------- Hourly Weather(바람) ---------\n")
        data.hourly?.forEach { hourly ->
            Log.d("####", "시간 : ${hourly.dt.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDDHHMMSS)} " +
                    "바람 : (${WindType.values().getOrNull((((hourly.windDeg) + 22.5 * 0.5) / 22.5).toInt())?.codeNm})${hourly.windSpeed}m/s\"")
        }

        Log.d("####", "\n--------- Hourly Weather(강수) ---------\n")
        data.hourly?.forEach { hourly ->
            Log.d("####", "시간 : ${hourly.dt.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDDHHMMSS)} 강수 : ${hourly.pop}")
        }

        Log.d("####", "\n--------- daily Weather ---------\n")
        data.daily?.forEach { daliy ->
            Log.d("####", "시간 : ${daliy.dt?.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDD)} 온도 - 최저 : ${daliy.temp?.min} / 최고 : ${daliy.temp?.max} / 강수확률 : ${daliy.pop}")
        }
    }

    /**
     * 참고 : https://hgko1207.github.io/2020/07/31/java-dev-3/
     */
    enum class WindType(
        val code :Int,
        val codeNm: String
    ) {
        N0(0, "북"),
        NNE(1, "북북동"),
        NE(2, "북동"),
        ENE(3, "동북동"),
        E(4, "동"),
        ESE(5, "동남동"),
        SE(6, "남동"),
        SSE(7, "남남동"),
        S(8, "남"),
        SSW(9, "남남서"),
        SW(10, "남서"),
        WSW(11, "서남서"),
        W(12, "서"),
        WNW(13, "서북서"),
        NW(14, "북서"),
        NNW(15, "북북서"),
        N16(16, "북")
    }
}