package com.jihun.searchweather.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jihun.searchweather.data.Landing
import com.jihun.searchweather.data.RouterEvent
import com.jihun.searchweather.databinding.ActivityMainBinding
import com.jihun.searchweather.ui.LinearLayoutManagerWrapper
import com.jihun.searchweather.util.LandingRouter
import com.jihun.searchweather.util.hideKeyboard
import com.jihun.searchweather.util.setItemAnimatorDuration
import io.reactivex.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initRecyclerView()
        initViewModel()

    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun initView() {

        with(binding) {
            etSearch.apply {
                setOnKeyListener { _, keyCode, event ->
                    if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        hideKeyboard(this)
                        mainViewModel.searchCityInfo(etSearch.text.trim().toString())
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
                            compositeDisposable.clear()
                            mainViewModel.onNextObservable(it)
                        }
                    }
                })
            }
            searchRemove.setOnClickListener { removeEditText() }
            moveToTop.setOnClickListener { binding.recyclerView.scrollToPosition(0) }
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
            (binding.recyclerView.adapter as? CityListAdapter)?.items = it.map { it.copy() }.toMutableList()
        })
        mainViewModel.searchLiveData.observe(this, Observer {
            it?.let {
                val lat = it.coord?.lat ?: 0.0
                val long = it.coord?.lon ?: 0.0

                if (lat == 0.0 || long == 0.0) {
                    Toast.makeText(this@MainActivity, "도시를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                    return@Observer
                }

                LandingRouter.move(
                    this,
                    RouterEvent(type = Landing.DETAIL, lat = lat, long = long, city = it.name)
                )
            } ?: run {
                Toast.makeText(this@MainActivity, "도시를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        mainViewModel.getCityData(this)
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManagerWrapper(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = CityListAdapter(compositeDisposable).apply {
                registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
                    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                        scrollToPosition(0)
                    }
                })
            }

            itemAnimator = setItemAnimatorDuration(100L)

            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val itemIdx = (layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition()
                    showMoveTopBtn(!(itemIdx == 0 || itemIdx == null || adapter?.itemCount == 0))
                }
            })

            setOnTouchListener { v, event ->
                hideKeyboard(binding.etSearch)
                false
            }
        }
    }
    private fun showMoveTopBtn(isShow: Boolean) { if (isShow) binding.moveToTop.show() else binding.moveToTop.hide() }
}