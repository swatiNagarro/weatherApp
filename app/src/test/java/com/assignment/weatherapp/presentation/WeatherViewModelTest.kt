package com.assignment.weatherapp.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.assignment.weatherapp.TestCoroutineRule
import com.assignment.weatherapp.domain.*
import com.assignment.weatherapp.network.Resource
import com.assignment.weatherapp.repository.WeatherRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WeatherViewModelTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var weatherRepo: WeatherRepo

    private lateinit var weatherViewModel: WeatherViewModel


    @Mock
    private lateinit var liveDataObserver: Observer<Resource<WeatherData>>


    @Before
    fun setup() {
        weatherViewModel = WeatherViewModel(weatherRepo)
    }

    private fun getWeatherData(): WeatherData {
        return WeatherData(
            "", Clouds(1), 1, Coord(1.1, 1.2),
            11, 12, Main(
                2.2, 1, 1, 1, 1, 2.2, 2.3,
                2.4
            ), "", Sys("", 1, 1), 123, 123, emptyList(),
            Wind(1, 1.1, 2.3)
        )
    }

    @Test
    fun getWeatherDataShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            doReturn(getWeatherData())
                .`when`(weatherRepo)
                .getWeatherData("", "")
            weatherViewModel.getData().observeForever(liveDataObserver)
            verify(weatherRepo).getWeatherData(anyString(), anyString())
            verify(liveDataObserver).onChanged(Resource.Success(any()))
            weatherViewModel.getData().removeObserver(liveDataObserver)
        }
    }


    @Test
    fun getWeatherDataShouldReturnError() {
        testCoroutineRule.runBlockingTest {
            val errorMessage = "Error Message For You"
            doThrow(RuntimeException(errorMessage))
                .`when`(weatherRepo)
                .getWeatherData("", "")

            weatherViewModel.getData().observeForever(liveDataObserver)
            verify(weatherRepo).getWeatherData(anyString(), anyString())
            verify(liveDataObserver).onChanged(
                Resource.Failure(false,
                    404,
                    null
                )
            )
            weatherViewModel.getData().removeObserver(liveDataObserver)
        }
    }

}