package com.example.weatherapp.core.api

import com.example.weatherapp.core.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun fetchWeatherData(): Flow<Result<Weather>>
}