package com.example.nasaapp.data.repository

import com.example.nasaapp.data.dto.ApodResponse


interface ApodRepository {
    suspend fun getApod(apiKey: String, date: String? = null): ApodResponse
}