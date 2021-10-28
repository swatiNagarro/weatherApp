package com.assignment.weatherapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.weatherapp.domain.WeatherData
import com.assignment.weatherapp.network.Resource
import com.assignment.weatherapp.repository.WeatherRepo
import kotlinx.coroutines.launch

class WeatherViewModel(private val weatherRepo: WeatherRepo) : ViewModel() {
    var liveData: LiveData<Resource<WeatherData>>
    private var mutableLiveData: MutableLiveData<Resource<WeatherData>> =
        MutableLiveData<Resource<WeatherData>>()

    init {
        liveData = mutableLiveData
    }

    fun getWeatherData(zipCode: String, api_key: String) {
        viewModelScope.launch {
            mutableLiveData.value = weatherRepo.getWeatherData(
                "$zipCode,in",
                api_key
            )
        }
        liveData = mutableLiveData
    }

}