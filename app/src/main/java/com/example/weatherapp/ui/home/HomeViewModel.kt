package com.example.weatherapp.ui.home

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R
import com.example.weatherapp.core.api.WeatherRepository
import com.example.weatherapp.core.model.Weather
import com.example.weatherapp.data.ErrorType
import com.example.weatherapp.data.Result
import com.example.weatherapp.data.PollingService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val pollingService: PollingService
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenViewState(isLoading = true))
    val state: StateFlow<HomeScreenViewState> = _state.asStateFlow()

    fun processIntent(homeScreenIntent: HomeScreenIntent) {
        when (homeScreenIntent) {
            is HomeScreenIntent.LoadWeatherData -> {
                viewModelScope.launch {
                    weatherRepository.fetchWeatherData().collect { result ->
                        processResult(result)
                    }
                }
            }

            is HomeScreenIntent.CancelWeatherDataPolling -> {
                pollingService.stopPolling()
            }

            is HomeScreenIntent.DisplayCityName -> {
                setState { copy(cityName = homeScreenIntent.cityName) }
            }
        }
    }

    private fun processResult(result: Result<Weather>) {
        when (result) {
            is Result.Success -> {
                val weatherData = result.data
                setState {
                    copy(
                        weather = weatherData,
                        isLoading = false,
                        error = null
                    )
                }
            }

            is Result.Error -> {
                setState {
                    copy(
                        isLoading = false,
                        error = mapErrorTypeToResource(result.errorType)
                    )
                }
            }

            else -> {}
        }
    }

    private fun mapErrorTypeToResource(errorType: ErrorType): Int = when (errorType) {
        ErrorType.GENERIC -> R.string.error_generic
        ErrorType.IO_CONNECTION -> R.string.error_client
        ErrorType.UNAUTHORIZED -> R.string.error_unauthorized
        ErrorType.CLIENT -> R.string.error_client
        ErrorType.SERVER -> R.string.error_server
    }

    private fun setState(stateReducer: HomeScreenViewState.() -> HomeScreenViewState) {
        viewModelScope.launch {
            _state.emit(stateReducer(state.value))
        }
    }
}

data class HomeScreenViewState(
    val cityName: String = "-",
    val weather: Weather? = null,
    val isLoading: Boolean = false,
    @StringRes val error: Int? = null
)