package com.jihun.searchweather.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jihun.searchweather.api.RetrofitClient
import com.jihun.searchweather.data.DetailInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class DetailViewModel: ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    private val _detailLiveData: MutableLiveData<DetailInfo> = MutableLiveData()
    val detailLiveData: LiveData<DetailInfo>
        get() = _detailLiveData

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getWeatherData(lat: Double, long: Double) {
        RetrofitClient.getInstance().getService().requestGetOneCallWeatherData(lat, long)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("####", "Success")
                _detailLiveData.value = it
            }, {
                Log.d("####", "Fail > ${it.message}")
            })
            .addTo(compositeDisposable)
    }
}