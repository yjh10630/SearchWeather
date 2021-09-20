package com.jihun.searchweather.ui.main.holder

import android.view.View
import com.jihun.searchweather.data.CityHeader
import com.jihun.searchweather.databinding.ViewCityHeaderBinding
import com.jihun.searchweather.ui.base.BaseViewHolder

class CityHeaderViewHolder(private val binding: ViewCityHeaderBinding): BaseViewHolder(binding.root) {
    override fun onBind(data: Any?) {
        (data as? CityHeader)?.let {
            initView(it)
        } ?: run {
            binding.divider.visibility = View.GONE
        }
    }

    private fun initView(data: CityHeader) {
        with(binding) {
            tvHeaderNm.text = data.name ?: ""
            tvCount.text = "(${data.count})"
        }
    }
}