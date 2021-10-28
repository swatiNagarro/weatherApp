package com.assignment.weatherapp.repository

import com.assignment.weatherapp.network.WeatherAPIService

open class WeatherRepo(
    private val api: WeatherAPIService
) : BaseWeatherRepo() {

    open suspend fun getWeatherData(zipCode: String, apiKey: String) = safeApiCall {
        api.getWeatherDataAsPerLocation(zipCode, apiKey)
    }


}