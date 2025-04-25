package com.example.nasaapp.domain.usecase

import com.example.nasaapp.data.dto.ApodResponse
import com.example.nasaapp.data.repository.ApodRepository

class GetApodUseCase(private val repository: ApodRepository) {
    suspend operator fun invoke(apiKey: String, date: String? = null): ApodResponse {
        return repository.getApod(apiKey, date)
    }
}