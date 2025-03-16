package com.example.nasaapp.di

import com.example.nasaapp.BuildConfig
import com.example.nasaapp.domain.repository.ApodRepositoryImpl
import com.example.nasaapp.data.api.NasaApiService
import com.example.nasaapp.data.repository.ApodRepository
import com.example.nasaapp.presentation.ui.viewmodel.ApodViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(NasaApiService::class.java) }
}

val repositoryModule = module {
    single<ApodRepository> { ApodRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModel { ApodViewModel(get()) }
}