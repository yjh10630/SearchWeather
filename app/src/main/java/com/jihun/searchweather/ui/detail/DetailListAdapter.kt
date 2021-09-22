package com.jihun.searchweather.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jihun.searchweather.ViewType
import com.jihun.searchweather.data.DetailModule
import com.jihun.searchweather.databinding.*
import com.jihun.searchweather.ui.base.BaseViewHolder
import com.jihun.searchweather.ui.base.DividerViewHolder
import com.jihun.searchweather.ui.base.EmptyViewHolder
import com.jihun.searchweather.ui.detail.holder.DetailCurrentWeatherViewHolder
import com.jihun.searchweather.ui.detail.holder.DetailDailyWeatherViewHolder
import com.jihun.searchweather.ui.detail.holder.DetailHourlyWeatherViewHolder

class DetailListAdapter: RecyclerView.Adapter<BaseViewHolder>() {
    var items: MutableList<DetailModule>? = null
        set(value) {
            value?.let {
                field?.let {
                    it.clear()
                    it.addAll(value)
                } ?: run {
                    field = value
                }
            }
            notifyDataSetChanged()
        }
    override fun getItemCount(): Int = items?.size ?: 0
    override fun getItemViewType(position: Int): Int = items?.getOrNull(position)?.type?.ordinal ?: ViewType.EMPTY.ordinal
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        when(ViewType.values()[viewType]) {
            ViewType.DETAIL_CURRENT_WEATHER -> DetailCurrentWeatherViewHolder(
                ViewDetailCurrentWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            ViewType.DIVIDER -> DividerViewHolder(
                ViewDividerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            ViewType.DETAIL_DAILY_WEATHER -> DetailDailyWeatherViewHolder(
                ViewDetailDailyWeahterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            ViewType.DETAIL_HOURLY_WEATHER -> DetailHourlyWeatherViewHolder(
                ViewDetailHourlyWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> EmptyViewHolder(ViewEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val data = items?.getOrNull(position)
        holder.onBind(data?.data)
    }

}