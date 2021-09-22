package com.jihun.searchweather.ui.base

import com.jihun.searchweather.databinding.ViewEmptyBinding
import com.jihun.searchweather.ui.base.BaseViewHolder

class EmptyViewHolder(private val binding: ViewEmptyBinding): BaseViewHolder(binding.root) {
    override fun onBind(data: Any?) {}
}