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
            add(DetailModule(type = ViewType.DIVIDER))
            validateToAddHourlyWeather(data)
            add(DetailModule(type = ViewType.DIVIDER))
            validateToAddDailyWeather(data)
        }

        return modules
    }

    private fun MutableList<DetailModule>.validateToAddCurrentWeather(data: DetailInfo) {
        val current = data.current ?: return
        val weather = current.weather?.getOrNull(0)

        val currentData = DetailCurrentWeather(
            temp = "${current.temp.toInt()}??",
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
            val firstDt = hourlyList?.getOrNull(0)?.dt
            var num = firstDt?.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDD)?.getDiffDate(data.timezone, DATE_FORMAT_YYYYMMDD) ?: 0 // ?????? ?????????
            add(TimeValueData(type = ViewType.DETAIL_HOURLY_DATE_DIVIDER, value = firstDt?.convertToDate(data.timezone, DATE_FORMAT_MMDOTDD)))
            hourlyList.forEach {
                val dateDiff = it.dt.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDD)?.getDiffDate(data.timezone, DATE_FORMAT_YYYYMMDD) ?: 0
                if (num != dateDiff) {
                    add(TimeValueData(type = ViewType.DETAIL_HOURLY_DATE_DIVIDER, value = it.dt.convertToDate(data.timezone, DATE_FORMAT_MMDOTDD)))
                    num = dateDiff
                }

                add(TimeValueData(
                    time = it.dt.convertToDate(data.timezone, DATE_FORMAT_HH),
                    value = "${it.temp.toInt()}??",
                    iconUrl = "http://openweathermap.org/img/wn/${it.weather?.getOrNull(0)?.icon ?: ""}@2x.png"
                ))
            }
        }

        val humList = mutableListOf<TimeValueData>().apply {
            val firstDt = hourlyList?.getOrNull(0)?.dt
            var num = firstDt?.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDD)?.getDiffDate(data.timezone, DATE_FORMAT_YYYYMMDD) ?: 0 // ?????? ?????????
            add(TimeValueData(type = ViewType.DETAIL_HOURLY_DATE_DIVIDER, value = firstDt?.convertToDate(data.timezone, DATE_FORMAT_MMDOTDD)))
            hourlyList.forEach {

                val dateDiff = it.dt.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDD)?.getDiffDate(data.timezone, DATE_FORMAT_YYYYMMDD) ?: 0
                if (num != dateDiff) {
                    add(TimeValueData(type = ViewType.DETAIL_HOURLY_DATE_DIVIDER, value = it.dt.convertToDate(data.timezone, DATE_FORMAT_MMDOTDD)))
                    num = dateDiff
                }

                add(TimeValueData(
                    time = it.dt.convertToDate(data.timezone, DATE_FORMAT_HH),
                    value = "${it.humidity}%"
                ))
            }
        }

        val windList = mutableListOf<TimeValueData>().apply {
            val firstDt = hourlyList?.getOrNull(0)?.dt
            var num = firstDt?.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDD)?.getDiffDate(data.timezone, DATE_FORMAT_YYYYMMDD) ?: 0 // ?????? ?????????
            add(TimeValueData(type = ViewType.DETAIL_HOURLY_DATE_DIVIDER, value = firstDt?.convertToDate(data.timezone, DATE_FORMAT_MMDOTDD)))
            hourlyList.forEach {

                val dateDiff = it.dt.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDD)?.getDiffDate(data.timezone, DATE_FORMAT_YYYYMMDD) ?: 0
                if (num != dateDiff) {
                    add(TimeValueData(type = ViewType.DETAIL_HOURLY_DATE_DIVIDER, value = it.dt.convertToDate(data.timezone, DATE_FORMAT_MMDOTDD)))
                    num = dateDiff
                }

                add(TimeValueData(
                    time = it.dt.convertToDate(data.timezone, DATE_FORMAT_HH),
                    //value = "(${WindType.values().getOrNull((((it.windDeg) + 22.5 * 0.5) / 22.5).toInt())?.codeNm})${it.windSpeed}m/s"
                    value = "${it.windSpeed}m/s"
                ))
            }
        }

        val popList = mutableListOf<TimeValueData>().apply {
            val firstDt = hourlyList?.getOrNull(0)?.dt
            var num = firstDt?.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDD)?.getDiffDate(data.timezone, DATE_FORMAT_YYYYMMDD) ?: 0 // ?????? ?????????
            add(TimeValueData(type = ViewType.DETAIL_HOURLY_DATE_DIVIDER, value = firstDt?.convertToDate(data.timezone, DATE_FORMAT_MMDOTDD)))
            hourlyList.forEach {

                val dateDiff = it.dt.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDD)?.getDiffDate(data.timezone, DATE_FORMAT_YYYYMMDD) ?: 0
                if (num != dateDiff) {
                    add(TimeValueData(type = ViewType.DETAIL_HOURLY_DATE_DIVIDER, value = it.dt.convertToDate(data.timezone, DATE_FORMAT_MMDOTDD)))
                    num = dateDiff
                }

                add(TimeValueData(
                    time = it.dt.convertToDate(data.timezone, DATE_FORMAT_HH),
                    value = "${it.pop.toInt()}%"
                ))
            }
        }

        val tabList = mutableListOf<TabItem>().apply {
            if (!tempList.isNullOrEmpty()) this.add(TabItem(tabNm = "??????", isSelected = true, contentList = tempList))
            if (!humList.isNullOrEmpty()) this.add(TabItem(tabNm = "??????", contentList = humList))
            if (!windList.isNullOrEmpty()) this.add(TabItem(tabNm = "??????", contentList = windList))
            if (!popList.isNullOrEmpty()) this.add(TabItem(tabNm = "??????", contentList = popList))
        }

        val hourlyData = DetailHourlyWeather(tabList = tabList)

        add(DetailModule(type = ViewType.DETAIL_HOURLY_WEATHER, data = hourlyData))
    }

    private fun MutableList<DetailModule>.validateToAddDailyWeather(data: DetailInfo) {
        val daily = data.daily
        if (daily.isNullOrEmpty()) return

        daily.forEach {

            val day = it.dt.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDD)?.getDateDay(data.timezone, DATE_FORMAT_YYYYMMDD)

            val dailyData = DetailDailyWeather(
                date = "$day ${it.dt.convertToDate(data.timezone, DATE_FORMAT_MMDOTDD)}",
                iconUrl = "http://openweathermap.org/img/wn/${it.weather?.getOrNull(0)?.icon ?: ""}@2x.png",
                tempMin = "${it.temp?.min?.toInt()}??",
                tempMax = "${it.temp?.max?.toInt()}??",
                pop = "${(it.pop*100).toInt()}%"
            )
            add(DetailModule(type = ViewType.DETAIL_DAILY_WEATHER, data = dailyData))
        }
    }

    /**
     * ????????? ?????????
     */
    private fun setLogCheckData(data: DetailInfo) {
        Log.d("####", "--------- current Weather ---------")
        Log.d("####", "\n" +
                "?????? : ${data.current?.temp}\n" +
                "?????? : ${data.current?.weather?.getOrNull(0)?.description}\n" +
                "????????? : ${data.current?.weather?.getOrNull(0)?.icon}\n" +
                "?????? : ${data.current?.humidity}\n" +
                "?????? ?????? : ${data.current?.clouds}\n" +
                "?????? ?????? : ${data.current?.weather?.getOrNull(0)?.main}\n" +
                "?????? : ${data.current?.sunrise?.convertToDate(data.timezone, DATE_FORMAT_HHMM)}\n" +
                "?????? : ${data.current?.sunset?.convertToDate(data.timezone, DATE_FORMAT_HHMM)}\n" +
                "?????? : (${WindType.values().getOrNull((((data.current?.windDeg ?: 0) + 22.5 * 0.5) / 22.5).toInt())?.codeNm})${data.current?.windSpeed}m/s"
        )

        Log.d("####", "\n--------- Hourly Weather(??????) ---------\n")
        data.hourly?.forEach { hourly ->
            Log.d("####", "???????????? : ${hourly.dt.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDD000000)?.getDiffDate(data.timezone, DATE_FORMAT_YYYYMMDD000000)} ?????? : ${hourly.temp}")
        }

        Log.d("####", "\n--------- Hourly Weather(??????) ---------\n")
        data.hourly?.forEach { hourly ->
            Log.d("####", "?????? : ${hourly.dt.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDDHHMMSS)} ?????? : ${hourly.humidity}")
        }

        Log.d("####", "\n--------- Hourly Weather(??????) ---------\n")
        data.hourly?.forEach { hourly ->
            Log.d("####", "?????? : ${hourly.dt.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDDHHMMSS)} " +
                    "?????? : (${WindType.values().getOrNull((((hourly.windDeg) + 22.5 * 0.5) / 22.5).toInt())?.codeNm})${hourly.windSpeed}m/s\"")
        }

        Log.d("####", "\n--------- Hourly Weather(??????) ---------\n")
        data.hourly?.forEach { hourly ->
            Log.d("####", "?????? : ${hourly.dt.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDDHHMMSS)} ?????? : ${hourly.pop}")
        }

        Log.d("####", "\n--------- daily Weather ---------\n")
        data.daily?.forEach { daliy ->

            val diff = daliy.dt.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDD)?.getDiffDate(data.timezone, DATE_FORMAT_YYYYMMDD)
            val date = when(diff) {
                0 -> "??????"
                1 -> "??????"
                else -> daliy.dt.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDD)?.getDateDay(data.timezone, DATE_FORMAT_YYYYMMDD)
            }

            Log.d("####", "?????? : ($date)${daliy.dt?.convertToDate(data.timezone, DATE_FORMAT_YYYYMMDD)} ?????? - ?????? : ${daliy.temp?.min} / ?????? : ${daliy.temp?.max} / ???????????? : ${daliy.pop}")
        }
    }

    /**
     * ?????? : https://hgko1207.github.io/2020/07/31/java-dev-3/
     */
    enum class WindType(
        val code :Int,
        val codeNm: String
    ) {
        N0(0, "???"),
        NNE(1, "?????????"),
        NE(2, "??????"),
        ENE(3, "?????????"),
        E(4, "???"),
        ESE(5, "?????????"),
        SE(6, "??????"),
        SSE(7, "?????????"),
        S(8, "???"),
        SSW(9, "?????????"),
        SW(10, "??????"),
        WSW(11, "?????????"),
        W(12, "???"),
        WNW(13, "?????????"),
        NW(14, "??????"),
        NNW(15, "?????????"),
        N16(16, "???")
    }
}