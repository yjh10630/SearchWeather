package com.jihun.searchweather.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jihun.searchweather.databinding.ActivityMainBinding
import com.jihun.searchweather.util.hideKeyboard

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initRecyclerView()
        initViewModel()

    }

    private fun initView() {

        with(binding) {
            etSearch.apply {
                setOnKeyListener { _, keyCode, event ->
                    if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        //todo 검색
                        Toast.makeText(this@MainActivity, "검색 > ${etSearch.text.toString()}", Toast.LENGTH_SHORT).show()
                        hideKeyboard(this)
                        return@setOnKeyListener true
                    }
                    return@setOnKeyListener false
                }

                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) { }
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        s?.let {
                            when (it.isEmpty()) {
                                true -> { binding.searchRemove.visibility = View.GONE }
                                false -> { binding.searchRemove.visibility = View.VISIBLE }
                            }
                        }
                    }
                })
            }

            searchRemove.setOnClickListener { removeEditText() }

        }
    }

    private fun removeEditText() {
        with (binding) {
            if (etSearch.text.isEmpty()) return
            etSearch.text.clear()
            searchRemove.visibility = View.GONE
        }
    }

    private fun initViewModel() {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mainViewModel.cityLiveData.observe(this, Observer {

        })

        mainViewModel.getCityData(this)
    }

    private fun initRecyclerView() {

        binding.recyclerView.apply {

            setOnTouchListener { v, event ->
                hideKeyboard(binding.etSearch)
                false
            }
        }
    }

}