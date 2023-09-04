package com.example.koinandroid

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.koinandroid.Constants.API_KEY

class MainViewModel(
    private val apiService: BasicApiService,
    private val rxSingleSchedulers: RxSingleSchedulers
) : ViewModel() {

    val daysForecastList = MutableLiveData<List<Data>>()
    val errorMessage = MutableLiveData<String>()

    private val TAG = "MainViewModel"


    @SuppressLint("CheckResult")
    fun getDaysForecast() { //lat=44.34&lon=10.99
        apiService.getDaysForecast(30.0596113, 31.1760619, 16, API_KEY, "metric")
            .compose(rxSingleSchedulers.applySchedulers())
            .subscribe({
                if (it.list.isNotEmpty()) {
                    daysForecastList.value = it.list
                }
            }, {
                errorMessage.value = it.message
                Log.d(TAG, "getDaysForecast: OnError ${it.message}")
            })
    }
}