package com.example.nasaapp.app

import android.app.Application
import com.example.nasaapp.di.networkModule
import com.example.nasaapp.di.repositoryModule
import com.example.nasaapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NASAApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NASAApp)
            modules(listOf(networkModule, repositoryModule, viewModelModule))
        }
    }
}