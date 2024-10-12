package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.api.SharedPreferencesInteractor
import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.domain.db.TracksDbInteractor
import com.practicum.playlistmaker.domain.impl.PlayerInteractorlmpl
import com.practicum.playlistmaker.domain.impl.SharedPreferencesInteractorImpl
import com.practicum.playlistmaker.domain.impl.TrackInteractorImpl
import com.practicum.playlistmaker.domain.impl.TracksDbInteractorImpl
import org.koin.dsl.module

val InteractorModule = module {
    single<SharedPreferencesInteractor>(createdAtStart = true) {
        SharedPreferencesInteractorImpl(get())
    }

    single<TrackInteractor> {
        TrackInteractorImpl(get())
    }

    single<PlayerInteractor> {
        PlayerInteractorlmpl()
    }

    single<TracksDbInteractor> {
        TracksDbInteractorImpl(get())
    }
}