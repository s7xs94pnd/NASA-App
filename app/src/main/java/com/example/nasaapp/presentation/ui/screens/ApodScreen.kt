package com.example.nasaapp.presentation.ui.screens

import android.util.Log
import com.example.nasaapp.presentation.ui.components.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import com.example.nasaapp.BuildConfig
import com.example.nasaapp.presentation.ui.viewmodel.ApodState
import com.example.nasaapp.presentation.ui.viewmodel.ApodViewModel
import com.example.nasaapp.presentation.utils.getCurrentDate
import java.util.*

@Composable
fun ApodScreen(viewModel: ApodViewModel) {
    val apodState by viewModel.apodState.collectAsState()
    var showFullImage by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(getCurrentDate()) }
    val scrollState = rememberScrollState()

    when (apodState) {
        is ApodState.Loading -> {
            Log.d("ApodScreen", "Loading APOD data...")
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
        is ApodState.Success -> {
            val apod = (apodState as ApodState.Success).apod
            Log.d("ApodScreen", "APOD data fetched successfully: $apod")
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .animateContentSize(tween(durationMillis = 500))
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = apod.title,
                    style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                AsyncImage(
                    model = apod.imageHD,
                    contentDescription = "Astronomy Picture of the Day",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (showFullImage) 350.dp else 200.dp)
                        .clickable { showFullImage = !showFullImage }
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = apod.explanation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    text = apod.date,
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                )
                DatePicker(selectedDate, onDateSelected = { date ->
                    selectedDate = date
                    viewModel.getApod(BuildConfig.API, date = selectedDate)
                })
            }
        }

        is ApodState.Error -> {
            Log.e(
                "ApodScreen",
                "Error fetching APOD data: ${(apodState as ApodState.Error).message}"
            )
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = (apodState as ApodState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.getApod(BuildConfig.API) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Retry", color = Color.White)
                    }
                }
            }
        }
    }
}