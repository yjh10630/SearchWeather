package com.jihun.searchweather.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jihun.searchweather.data.INTENT_EXTRA_STRING_PARAM
import com.jihun.searchweather.databinding.ActivityDetailBinding

class DetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchKeyword = intent?.getStringExtra(INTENT_EXTRA_STRING_PARAM) ?: ""
        binding.txt.text = searchKeyword
    }
}