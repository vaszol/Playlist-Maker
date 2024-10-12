package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.data.SharedPreferencesRepositoryImpl
import com.practicum.playlistmaker.data.TrackDbConvertor
import com.practicum.playlistmaker.data.TrackDbRepositoryImpl
import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.domain.api.SharedPreferencesRepository
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.db.TrackDbRepository
import org.koin.dsl.module

val RepositoryModule = module {
    factory { TrackDbConvertor() }
    single<SharedPreferencesRepository> {
        SharedPreferencesRepositoryImpl(get(), get(), get())
    }

    single<TrackRepository> {
        TracksRepositoryImpl(get(), get(), get())
    }

    single<TrackDbRepository> {
        TrackDbRepositoryImpl(get(), get())
    }
}