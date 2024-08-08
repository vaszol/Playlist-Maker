package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.data.SharedPreferencesRepositoryImpl
import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.domain.api.SharedPreferencesRepository
import com.practicum.playlistmaker.domain.api.TrackRepository
import org.koin.dsl.module

val RepositoryModule = module {
    single<SharedPreferencesRepository> {
        SharedPreferencesRepositoryImpl(get(), get())
    }

    single<TrackRepository> {
        TracksRepositoryImpl(get())
    }
}