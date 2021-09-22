package com.jihun.searchweather.ui.main.holder

import android.view.View
import android.widget.Toast
import com.jihun.searchweather.data.CityInfo
import com.jihun.searchweather.data.Landing
import com.jihun.searchweather.data.RouterEvent
import com.jihun.searchweather.databinding.ViewCityItemBinding
import com.jihun.searchweather.ui.base.BaseViewHolder
import com.jihun.searchweather.util.LandingRouter

class CityItemViewHolder(private val binding: ViewCityItemBinding): BaseViewHolder(binding.root) {
    override fun onBind(data: Any?) {
        (data as? CityInfo)?.let {
            initView(it)
        }
    }

    private fun initView(data: CityInfo) {
        /*binding.name.text = if (data.inputKeyword?.isEmpty() == true) {
            data.name ?: ""
        } else {
            getSpannedColorText(
                origin = data.name ?: "",
                changed = data.inputKeyword ?: "",
                color = Color.parseColor("#4d6ee4")
            )
        }*/
        binding.name.text = data.name ?: ""

        itemView.setOnClickListener {

            if (data.coord == null) {
                Toast.makeText(itemView.context, "어뜨카지.. 좌표가 없어 검색이 되지 않습니다...", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            LandingRouter.move(
                itemView.context,
                RouterEvent(type = Landing.DETAIL, lat = data.coord.lat, long = data.coord.lon, city = data.name)
            )
        }
    }
}