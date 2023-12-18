package com.example.weatherapp.ui.home

sealed class HomeScreenIntent {
    object LoadWeatherData : HomeScreenIntent()

    data class DisplayCityName(val cityName: String) : HomeScreenIntent()

    object CancelWeatherDataPolling : HomeScreenIntent()
}