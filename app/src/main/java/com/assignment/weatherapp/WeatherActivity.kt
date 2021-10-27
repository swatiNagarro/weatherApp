package com.assignment.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.assignment.weatherapp.network.WeatherAPI
import kotlinx.android.synthetic.main.activity_main.*

class WeatherActivity : AppCompatActivity() {

    private val weatherViewModel: WeatherViewModel by lazy {
        ViewModelProvider(this, viewModelFactory {
            WeatherViewModel(
                WeatherAPI.retrofitApi
            )
        })
            .get(WeatherViewModel::class.java)
    }


    private inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(aClass: Class<T>): T = f() as T
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weather_btn.setOnClickListener {
            weatherViewModel.getWeatherData(
                editTextZipCode.text.toString(),
                BuildConfig.API_KEY
            )
            editTextZipCode.text.clear()
        }


        weatherViewModel.liveData.observe(this, {
            Log.d("test", "data = " + it.main.temp_max)
        })
    }
}