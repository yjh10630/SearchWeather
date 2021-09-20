package com.jihun.searchweather.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jihun.searchweather.data.LAT
import com.jihun.searchweather.data.LONG
import com.jihun.searchweather.databinding.ActivityDetailBinding
import com.jihun.searchweather.util.convertToDate

class DetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        val lat = intent?.getDoubleExtra(LAT, 0.0) ?: 0.0
        val long = intent?.getDoubleExtra(LONG, 0.0) ?: 0.0
        binding.txt.text = "lat : $lat long : $long"



        viewModel.detailLiveData.observe(this, Observer {
            binding.txt.text = "${binding.txt.text}\n${it.current?.temp}℃\n${it.current?.dt?.convertToDate(it.timezone)}"
        })

        viewModel.getWeatherData(lat, long)
    }
}