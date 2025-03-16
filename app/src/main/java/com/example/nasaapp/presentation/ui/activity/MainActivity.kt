package com.example.nasaapp.presentation.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.nasaapp.BuildConfig
import com.example.nasaapp.presentation.ui.screens.ApodScreen
import com.example.nasaapp.presentation.ui.viewmodel.ApodViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val apodViewModel: ApodViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApodScreen(viewModel = apodViewModel)
        }

        apodViewModel.getApod(BuildConfig.API)
        Log.d("MainActivity", "APOD ViewModel State: ${apodViewModel.apodState.value}")
    }
}