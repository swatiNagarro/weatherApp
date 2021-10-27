package com.assignment.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.weatherapp.domain.WeatherData
import com.assignment.weatherapp.network.WeatherAPIService
import kotlinx.coroutines.launch

class WeatherViewModel(private val weatherAPIService: WeatherAPIService) : ViewModel() {
    var liveData: LiveData<WeatherData>
    private var mutableLiveData: MutableLiveData<WeatherData> = MutableLiveData<WeatherData>()

    init {
        liveData = mutableLiveData
    }

    fun getWeatherData(zipCode: String, api_key: String) {
        viewModelScope.launch {
            mutableLiveData.value = weatherAPIService.getWeatherDataAsPerLocation(
                "$zipCode,in",
                api_key
            )
        }
        liveData = mutableLiveData
    }

}