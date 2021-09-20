package com.jihun.searchweather.ui.main.holder

import android.widget.Toast
import com.jihun.searchweather.data.CityInfo
import com.jihun.searchweather.databinding.ViewCityItemBinding
import com.jihun.searchweather.ui.base.BaseViewHolder

class CityItemViewHolder(private val binding: ViewCityItemBinding): BaseViewHolder(binding.root) {
    override fun onBind(data: Any?) {
        (data as? CityInfo)?.let {
            initView(it)
        }
    }

    private fun initView(data: CityInfo) {
        binding.name.text = data.name ?: ""
        itemView.setOnClickListener {
            Toast.makeText(itemView.context, "클릭됨 > ${data.name}", Toast.LENGTH_SHORT).show()
        }
    }
}