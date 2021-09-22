package com.jihun.searchweather.ui.detail.holder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jihun.searchweather.ViewType
import com.jihun.searchweather.data.DetailHourlyWeather
import com.jihun.searchweather.data.TabItem
import com.jihun.searchweather.data.TimeValueData
import com.jihun.searchweather.databinding.*
import com.jihun.searchweather.ui.base.BaseViewHolder
import com.jihun.searchweather.ui.base.EmptyViewHolder
import com.jihun.searchweather.util.HorizontalMarginDecoration
import com.jihun.searchweather.util.diffUpDate
import com.jihun.searchweather.util.toPx

class DetailHourlyWeatherViewHolder(private val binding: ViewDetailHourlyWeatherBinding): BaseViewHolder(binding.root) {
    override fun onBind(data: Any?) {
        (data as? DetailHourlyWeather)?.let {
            initView(it)
        }
    }

    private fun initView(data: DetailHourlyWeather) {
        initTab(data.tabList)
        initContent(data.tabList?.getOrNull(0)?.contentList)
    }

    private fun initContent(data: List<TimeValueData>?) {
        binding.contentRecyclerView.apply {
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ContentListAdapter().apply {
                items = data?.toMutableList()
            }
            if (itemDecorationCount == 0)
                addItemDecoration(HorizontalMarginDecoration(
                    firstItemMargin = 12.toPx,
                    lastItemMargin = 12.toPx,
                    itemMargin = 12.toPx
                ))
        }
    }

    private fun initTab(tabList: List<TabItem>?) {
        binding.tabRecyclerView.apply {
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = TabListAdapter {
                selectedTab(it)
            }.apply { items = tabList?.toMutableList() }
        }
    }

    private fun selectedTab(index: Int) {
        with(binding) {
            tabRecyclerView.apply {
                (adapter as? TabListAdapter)?.let {
                    val data = it.items?.map { it.copy() }?.toMutableList()
                    data?.first { it.isSelected }?.isSelected = false
                    data?.getOrNull(index)?.isSelected = true
                    it.items = data

                    (contentRecyclerView.adapter as? ContentListAdapter)?.items = data?.getOrNull(index)?.contentList?.toMutableList()
                    contentRecyclerView.scrollToPosition(0)
                }
            }
        }
    }

    /**
     * Tab
     */
    private inner class TabListAdapter(
        val callback: (Int) -> Unit
    ): RecyclerView.Adapter<BaseViewHolder>() {

        var items: MutableList<TabItem>? = null
            set(value) {
                value?.let {
                    diffUpDate(field, it,
                        itemCompare = { o, n ->
                            o?.tabNm == n?.tabNm
                        }, contentCompare = { o, n ->
                            o?.isSelected == n?.isSelected
                        })
                    field?.let {
                        it.clear()
                        it.addAll(value)
                    } ?: run {
                        field = value
                    }
                }
            }

        override fun getItemCount(): Int = items?.size ?: 0
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
            ItemViewHolder(ViewTabBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            holder.onBind(items?.getOrNull(position))
            holder.itemView.setOnClickListener {
                callback.invoke(position)
            }
        }
    }

    private inner class ItemViewHolder(private val binding: ViewTabBinding): BaseViewHolder(binding.root) {
        override fun onBind(data: Any?) {
            (data as? TabItem)?.let {
                binding.tvName.apply {
                    text = it.tabNm
                    setBackgroundColor(
                        Color.parseColor(
                        if (it.isSelected) "#3E009688" else "#FFFFFFFF"
                    ))
                }
            }
        }
    }

    /**
     * Content
     */
    private inner class ContentListAdapter: RecyclerView.Adapter<BaseViewHolder>() {
        var items: MutableList<TimeValueData>? = null
            set(value) {
                value?.let {
                    diffUpDate(field, it,
                        itemCompare = { o, n -> o?.type == n?.type },
                        contentCompare = { o, n -> o == n})
                    field?.let {
                        it.clear()
                        it.addAll(value)
                    } ?: run {
                        field = value
                    }
                }
            }
        override fun getItemCount(): Int = items?.size ?: 0
        override fun getItemViewType(position: Int): Int = items?.getOrNull(position)?.type?.ordinal ?: ViewType.EMPTY.ordinal
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
            when(ViewType.values()[viewType]) {
                ViewType.DETAIL_HOURLY_CONTENT -> ContentItemViewHolder(ViewDetailHourlyContentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
                ViewType.DETAIL_HOURLY_DATE_DIVIDER -> DateDividerViewHolder(
                    ViewDetailHourlyDateDividerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
                else -> EmptyViewHolder(ViewEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            holder.onBind(items?.getOrNull(position))
        }
    }

    private inner class DateDividerViewHolder(private val binding: ViewDetailHourlyDateDividerBinding): BaseViewHolder(binding.root) {
        override fun onBind(data: Any?) {
            (data as? TimeValueData)?.let {
                binding.tvName.text = it.value
            }
        }
    }

    private inner class ContentItemViewHolder(private val binding: ViewDetailHourlyContentBinding): BaseViewHolder(binding.root) {
        override fun onBind(data: Any?) {
            (data as? TimeValueData)?.let {
                with(binding) {
                    Glide.with(itemView.context)
                        .load(it.iconUrl)
                        .into(ivIcon)

                    tvTemp.text = it.value
                    tvDate.text = it.time
                }
            }
        }
    }
}