package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.data.ExternalStorageRepositoryImpl
import com.practicum.playlistmaker.data.NavigationRepositoryImpl
import com.practicum.playlistmaker.data.PlaylistDbConvertor
import com.practicum.playlistmaker.data.PlaylistRepositoryImpl
import com.practicum.playlistmaker.data.SharedPreferencesRepositoryImpl
import com.practicum.playlistmaker.data.TrackDbConvertor
import com.practicum.playlistmaker.data.TrackDbRepositoryImpl
import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.domain.ExternalStorageRepository
import com.practicum.playlistmaker.domain.NavigationRepository
import com.practicum.playlistmaker.domain.PlaylistRepository
import com.practicum.playlistmaker.domain.api.SharedPreferencesRepository
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.db.TrackDbRepository
import org.koin.dsl.module

val RepositoryModule = module {
    factory { TrackDbConvertor() }
    factory { PlaylistDbConvertor() }
    single<SharedPreferencesRepository> {
        SharedPreferencesRepositoryImpl(get(), get(), get())
    }

    single<TrackRepository> {
        TracksRepositoryImpl(get(), get(), get())
    }

    single<TrackDbRepository> {
        TrackDbRepositoryImpl(get(), get())
    }

    single<NavigationRepository> { NavigationRepositoryImpl() }

    single<ExternalStorageRepository> { ExternalStorageRepositoryImpl(get()) }

    single<PlaylistRepository> { PlaylistRepositoryImpl(get(), get(), get()) }
}