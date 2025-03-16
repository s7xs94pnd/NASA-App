package com.example.nasaapp.presentation.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasaapp.data.repository.ApodRepository
import com.example.nasaapp.data.dto.ApodResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ApodViewModel(private val repository: ApodRepository) : ViewModel() {

    private val _apodState = MutableStateFlow<ApodState>(ApodState.Loading)
    val apodState: StateFlow<ApodState> get() = _apodState

    fun getApod(apiKey: String, date: String? = null) {
        _apodState.value = ApodState.Loading
        viewModelScope.launch {
            try {
                Log.d("ApodViewModel", "Fetching APOD data...")
                val response = repository.getApod(apiKey, date)
                _apodState.value = ApodState.Success(response)
                Log.d("ApodViewModel", "APOD data fetched successfully: $response")
            } catch (e: Exception) {
                _apodState.value = ApodState.Error(e.message.orEmpty())
                Log.e("ApodViewModel", "Error fetching APOD data: ${e.message}", e)
            }
        }
    }
}

sealed class ApodState {
    object Loading : ApodState()
    data class Success(val apod: ApodResponse) : ApodState()
    data class Error(val message: String) : ApodState()
}