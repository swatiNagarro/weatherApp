package com.assignment.weatherapp.repository

import com.assignment.weatherapp.network.WeatherAPIService

class WeatherRepo(
    private val api: WeatherAPIService
) : BaseWeatherRepo() {

    suspend fun getWeatherData(zipCode: String, apiKey: String) = safeApiCall {
        api.getWeatherDataAsPerLocation(zipCode, apiKey)
    }


}