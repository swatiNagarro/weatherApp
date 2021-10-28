package com.assignment.weatherapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.assignment.weatherapp.network.Resource
import com.assignment.weatherapp.network.WeatherAPI
import com.assignment.weatherapp.repository.WeatherRepo
import kotlinx.android.synthetic.main.activity_main.*

class WeatherActivity : AppCompatActivity() {

    private val weatherViewModel: WeatherViewModel by lazy {
        ViewModelProvider(this, viewModelFactory {
            WeatherViewModel(
                WeatherRepo(WeatherAPI.retrofitApi)
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
            updateUIWhenGettingData()
            weatherViewModel.getWeatherData(
                editTextZipCode.text.toString(),
                BuildConfig.API_KEY
            )
            editTextZipCode.text.clear()
        }


        weatherViewModel.liveData.observe(this, {
            when (it) {
                is Resource.Success -> {
                    Log.d("test", "Received data" + it.value.name)
                    showData(it.value.name, it.value.weather[0].description)
                }
                is Resource.Failure -> {
                    showError()
                }
            }
        })
    }

    private fun updateUIWhenGettingData() {
        progressBar.visibility = View.VISIBLE
        weather_btn.visibility = View.GONE
        editTextZipCode.visibility = View.GONE
        weather_data.visibility = View.GONE
    }

    private fun showData(name: String, desc: String) {
        progressBar.visibility = View.GONE
        weather_btn.visibility = View.VISIBLE
        editTextZipCode.visibility = View.VISIBLE
        weather_data.visibility = View.VISIBLE
        city_name.text = name
        city_temp_desc.text = desc
    }

    private fun showError() {
        progressBar.visibility = View.GONE
        weather_btn.visibility = View.VISIBLE
        editTextZipCode.visibility = View.VISIBLE
        weather_data.visibility = View.GONE
        Toast.makeText(
            applicationContext, R.string.error_message,
            Toast.LENGTH_LONG
        ).show()
    }
}