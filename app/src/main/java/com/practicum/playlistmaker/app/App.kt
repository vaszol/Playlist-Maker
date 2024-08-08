package com.practicum.playlistmaker.app

import android.app.Application
import com.practicum.playlistmaker.di.DataModule
import com.practicum.playlistmaker.di.InteractorModule
import com.practicum.playlistmaker.di.RepositoryModule
import com.practicum.playlistmaker.di.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                DataModule,
                InteractorModule,
                RepositoryModule,
                ViewModelModule
            )
        }
    }
}