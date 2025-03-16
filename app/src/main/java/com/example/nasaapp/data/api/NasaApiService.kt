package com.example.nasaapp.data.api

import com.example.nasaapp.data.dto.ApodResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface NasaApiService {
    @GET("planetary/apod")
    suspend fun fetchApod(
        @Query("api_key") apiKey: String,
        @Query("date") date: String? = null
    ): Response<ApodResponse>
}