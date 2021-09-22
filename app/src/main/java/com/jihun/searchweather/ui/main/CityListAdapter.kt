package com.jihun.searchweather.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jihun.searchweather.ViewType
import com.jihun.searchweather.data.MainModule
import com.jihun.searchweather.databinding.ViewCityHeaderBinding
import com.jihun.searchweather.databinding.ViewCityItemBinding
import com.jihun.searchweather.databinding.ViewEmptyBinding
import com.jihun.searchweather.ui.base.BaseViewHolder
import com.jihun.searchweather.ui.main.holder.CityHeaderViewHolder
import com.jihun.searchweather.ui.main.holder.CityItemViewHolder
import com.jihun.searchweather.ui.base.EmptyViewHolder

class CityListAdapter: RecyclerView.Adapter<BaseViewHolder>() {
    var items: MutableList<MainModule>? = null
        set(value) {
            value?.let {
                field?.let {
                    it.clear()
                    it.addAll(value)
                } ?: run {
                    field = value
                }
            }
            //todo diffutil
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = items?.size ?: 0
    override fun getItemViewType(position: Int): Int = items?.getOrNull(position)?.type?.ordinal ?: ViewType.EMPTY.ordinal
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        when(ViewType.values()[viewType]) {
            ViewType.MAIN_ITEM_HEADER -> CityHeaderViewHolder(ViewCityHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            ViewType.MAIN_ITEM -> CityItemViewHolder(ViewCityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> EmptyViewHolder(ViewEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(items?.getOrNull(position)?.data)
    }
}