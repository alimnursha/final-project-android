package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.ui.home.HomeScreenIntent
import com.example.weatherapp.ui.home.HomeViewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherAppDestinationsConfig(
                        navController = rememberNavController(),
                        homeViewModel = homeViewModel
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        homeViewModel.processIntent(HomeScreenIntent.LoadWeatherData)
    }

    override fun onStop() {
        super.onStop()
        homeViewModel.processIntent(HomeScreenIntent.CancelWeatherDataPolling)
    }
}
