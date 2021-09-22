package com.jihun.searchweather.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jihun.searchweather.ViewType
import com.jihun.searchweather.data.CityHeader
import com.jihun.searchweather.data.CityInfo
import com.jihun.searchweather.data.MainModule
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.io.FileNotFoundException
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

class MainViewModel: ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }
    private var mainModuleDataSet: MutableList<MainModule>? = null

    private val _cityLiveData: MutableLiveData<MutableList<MainModule>> = MutableLiveData()
    val cityLiveData: LiveData<MutableList<MainModule>>
        get() = _cityLiveData

    fun onNextObservable(editTextString: CharSequence) {
        Observable.create<CharSequence> { emitter -> emitter.onNext(editTextString) }
            .debounce(200L, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.computation())
            .map(::createViewAutoComplete)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _cityLiveData.value = it
            }, {
                Log.d("####", "Fail > ${it.message}")
            })
            .addTo(compositeDisposable)
    }

    private fun createViewAutoComplete(txt: CharSequence): MutableList<MainModule>? {
        val module = mutableListOf<MainModule>()
        val cityData = mainModuleDataSet?.filter { it.type == ViewType.MAIN_ITEM }?.map { (it.data as? CityInfo) }?.toMutableList()

        cityData?.filter {
            it?.name?.lowercase()?.contains(txt.toString().lowercase()) == true
        }?.take(10)?.forEach {
            module.add(MainModule(type = ViewType.MAIN_ITEM, data = it))
        }

        return if (module.isNullOrEmpty()) mainModuleDataSet else module
    }

    fun getCityData(context: Context) {
        Observable.fromCallable { parseJson<List<CityInfo>>(context, "citylist.json") }
            .subscribeOn(Schedulers.io())
            .map(::createViewEntity)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mainModuleDataSet = it
                    _cityLiveData.value = it
                }, {
                    Log.d("####", "Fail > ${it.message}")
                }
            )
            .addTo(compositeDisposable)
    }

    private fun createViewEntity(data: List<CityInfo>): MutableList<MainModule> {
        val modules = mutableListOf<MainModule>()
        data.distinctBy { it.country }.forEach { sortCountry ->
            data.filter { it.country == sortCountry.country }.run {
                if (size != 0) {
                    modules.add(
                        MainModule(
                            type = ViewType.MAIN_ITEM_HEADER,
                            data = CityHeader(
                                name = sortCountry.country,
                                count = size)))

                    forEach { modules.add(
                        MainModule(
                            type = ViewType.MAIN_ITEM,
                            data = it))
                    }
                }
            }
        }
        return modules
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


    private inline fun <reified T> parseJson(context: Context, fileName: String): T {
        var json: String? = null // 파일 입력에 오류가 있을 경우의 예외처리
        try {
            json = context
                .assets
                .open(fileName)
                .bufferedReader()
                .use {
                    it.readText()
                }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val type: Type = object : TypeToken<T>() {}.type
        return Gson().fromJson(json, type)
    }
}