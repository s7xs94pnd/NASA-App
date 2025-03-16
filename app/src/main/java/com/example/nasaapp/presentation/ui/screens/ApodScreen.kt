package com.example.nasaapp.presentation.ui.screens

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.nasaapp.BuildConfig
import com.example.nasaapp.presentation.ui.viewmodel.ApodState
import com.example.nasaapp.presentation.ui.viewmodel.ApodViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ApodScreen(viewModel: ApodViewModel) {
    val apodState by viewModel.apodState.collectAsState()
    var showFullImage by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(getCurrentDate()) }

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
                    .animateContentSize(tween(durationMillis = 500)),
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

                apod.copyright?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "© $it",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
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
            Log.e("ApodScreen", "Error fetching APOD data: ${(apodState as ApodState.Error).message}")
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

fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(Calendar.getInstance().time)
}


@SuppressLint("DefaultLocale")
@Composable
fun DatePicker(selectedDate: String, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()


    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val formattedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                onDateSelected(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Box(
        modifier = Modifier
            .border(1.dp, Color.Black, CircleShape)
            .background(Color.Black, CircleShape)
            .clickable {
                datePickerDialog.show()
            }
            .padding(12.dp)
    ) {
        Text(
            text = "Selected Date: $selectedDate",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.padding(8.dp)
        )
    }
}