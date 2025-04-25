package com.example.nasaapp.di

import com.example.nasaapp.BuildConfig
import com.example.nasaapp.data.api.NasaApiService
import com.example.nasaapp.domain.repository.ApodRepositoryImpl
import com.example.nasaapp.data.repository.ApodRepository
import com.example.nasaapp.domain.usecase.GetApodUseCase
import com.example.nasaapp.presentation.ui.viewmodel.ApodViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { provideRetrofit(get()) }
    single { provideOkhttpClient() }
    single { get<Retrofit>().create(NasaApiService::class.java) }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideOkhttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
}

val repositoryModule = module {
    single<ApodRepository> { ApodRepositoryImpl(get()) }
}

val useCaseModule = module {
    single { GetApodUseCase(get()) }
}

val viewModelModule = module {
    viewModel { ApodViewModel(get()) }
}