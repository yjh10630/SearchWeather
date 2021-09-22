package com.jihun.searchweather.ui.detail.holder

import android.text.Spannable
import com.bumptech.glide.Glide
import com.jihun.searchweather.data.DetailCurrentWeather
import com.jihun.searchweather.databinding.ViewDetailCurrentWeatherBinding
import com.jihun.searchweather.ui.base.BaseViewHolder
import com.jihun.searchweather.util.getSpannedSizeText

class DetailCurrentWeatherViewHolder(
    private val binding: ViewDetailCurrentWeatherBinding
): BaseViewHolder(binding.root) {
    override fun onBind(data: Any?) {
        (data as? DetailCurrentWeather)?.let {
            initView(it)
        }
    }

    private fun initView(data: DetailCurrentWeather) {
        with(binding) {
            Glide.with(itemView.context)
                .load(data.iconUrl)
                .into(ivIcon)

            tvTemp.text = data.temp

            tvSkyStatus.text = data.description

            tvPop.text = "강수확률 : " styleChange "0%"

            tvHum.text = "습도 : " styleChange data.humidity
            tvWind.text = "바람 : " styleChange data.wind

            tvSunRise.text = "일출\n" styleChange data.sunrise
            tvSunSet.text = "일몰\n" styleChange data.sunset
        }
    }

    private infix fun String.styleChange(txt: String?): Spannable =
        getSpannedSizeText(
            origin = "$this$txt",
            changed = txt ?: "",
            sizeDp = 16,
            bold = true
        )
}