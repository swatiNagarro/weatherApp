package com.assignment.weatherapp.network

import com.assignment.weatherapp.domain.WeatherData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_WEATHER_URL =
    "https://api.openweathermap.org/data/2.5/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_WEATHER_URL)
    .build()


interface WeatherAPIService {

    @GET("weather")
    suspend fun getWeatherDataAsPerLocation(
        @Query("zip") zip: String,
        @Query("appid") key: String
    ): WeatherData
}

object WeatherAPI {
    val retrofitApi: WeatherAPIService by lazy { retrofit.create(WeatherAPIService::class.java) }
}