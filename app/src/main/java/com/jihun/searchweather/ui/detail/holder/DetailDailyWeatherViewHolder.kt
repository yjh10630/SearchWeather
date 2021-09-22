package com.jihun.searchweather.ui.detail.holder

import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.jihun.searchweather.data.DetailDailyWeather
import com.jihun.searchweather.databinding.ViewDetailDailyWeahterBinding
import com.jihun.searchweather.ui.base.BaseViewHolder
import com.jihun.searchweather.util.getSpannedColorText

class DetailDailyWeatherViewHolder(private val binding: ViewDetailDailyWeahterBinding): BaseViewHolder(binding.root) {
    override fun onBind(data: Any?) {
        (data as? DetailDailyWeather)?.let {
            initView(it)
        }
    }

    private fun initView(data: DetailDailyWeather) {
        with(binding) {
            Glide.with(itemView.context)
                .load(data.iconUrl)
                .into(ivIcon)

            tvDate.text = data.date
            tvPop.text = data.pop

            val min = getSpannedColorText(
                origin = data.tempMin ?: "",
                changed = data.tempMin ?: "",
                color = ContextCompat.getColor(itemView.context, android.R.color.holo_blue_light),
                bold = false
            )
            val max = getSpannedColorText(
                origin = data.tempMax ?: "",
                changed = data.tempMax ?: "",
                color = ContextCompat.getColor(itemView.context, android.R.color.holo_red_light),
                bold = true
            )
            tvTemp.text = TextUtils.concat(min, "/", max)
        }
    }
}