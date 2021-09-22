package com.jihun.searchweather.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jihun.searchweather.data.CITY
import com.jihun.searchweather.data.LAT
import com.jihun.searchweather.data.LONG
import com.jihun.searchweather.databinding.ActivityDetailBinding

class DetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lat = intent?.getDoubleExtra(LAT, 0.0) ?: 0.0
        val long = intent?.getDoubleExtra(LONG, 0.0) ?: 0.0
        val cityNm = intent?.getStringExtra(CITY) ?: ""

        initView(cityNm)
        initViewModel()

        viewModel.getWeatherData(lat, long)

    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.detailLiveData.observe(this, Observer {
            (binding.recyclerView.adapter as? DetailListAdapter)?.items = it
        })
    }

    private fun initView(cityNm: String) {
        with(binding) {
            ivBack.setOnClickListener { finish() }
            tvCityNm.text = cityNm

            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.VERTICAL, false)
                adapter = DetailListAdapter()
            }
        }
    }
}