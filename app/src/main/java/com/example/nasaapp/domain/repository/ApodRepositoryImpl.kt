package com.example.nasaapp.domain.repository

import android.util.Log
import com.example.nasaapp.data.api.NasaApiService
import com.example.nasaapp.data.dto.ApodResponse
import com.example.nasaapp.data.repository.ApodRepository

class ApodRepositoryImpl(private val apiService: NasaApiService) : ApodRepository {

    override suspend fun getApod(apiKey: String, date: String?): ApodResponse {
        return try {
            val response = apiService.fetchApod(apiKey, date)
            if (response.isSuccessful) {
                Log.d("ApodRepositoryImpl", "API call successful: ${response.body()}")
                response.body()!!
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("ApodRepositoryImpl", "API call failed: $errorBody")
                throw Exception("API call failed with error: $errorBody")
            }
        } catch (e: Exception) {
            Log.e("ApodRepositoryImpl", "Exception during API call: ${e.message}", e)
            throw e
        }
    }
}