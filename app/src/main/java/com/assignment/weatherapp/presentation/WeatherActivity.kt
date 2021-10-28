package com.assignment.weatherapp.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.assignment.weatherapp.BuildConfig
import com.assignment.weatherapp.R
import com.assignment.weatherapp.network.Resource
import com.assignment.weatherapp.network.WeatherAPI
import com.assignment.weatherapp.repository.WeatherRepo
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.round

const val celsius_symbol = 0x00B0

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


        weatherViewModel.liveData.observe(this, { resource ->
            when (resource) {
                is Resource.Success -> {
                    showData(
                        resource.value.name,
                        convertTempInCelsius(resource.value.main.temp).toString()
                    )
                }
                is Resource.Failure -> {
                    handleErrorScenarios(resource)

                }
            }
        })
    }

    private fun handleErrorScenarios(resource: Resource.Failure) {
        if (resource.isNetworkError) {
            showError(getString(R.string.error_message_internet_not_present))
        } else {
            showError(getString(R.string.error_message))
        }
    }

    private fun convertTempInCelsius(temp: Double): Double {
        return round(temp - 273.15)
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

        city_temp_desc.text = desc + celsius_symbol.toChar()
    }

    private fun showError(message: String) {
        progressBar.visibility = View.GONE
        weather_btn.visibility = View.VISIBLE
        editTextZipCode.visibility = View.VISIBLE
        weather_data.visibility = View.GONE
        Toast.makeText(
            applicationContext, message,
            Toast.LENGTH_LONG
        ).show()
    }
}