package com.practicum.playlistmaker.domain

import android.app.Application
import android.app.Application.MODE_PRIVATE
import com.practicum.playlistmaker.data.SharedPreferencesRepositoryImpl
import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.ItunesApiClient
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.api.SharedPreferencesInteractor
import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.impl.PlayerInteractorlmpl
import com.practicum.playlistmaker.domain.impl.SharedPreferencesInteractorImpl
import com.practicum.playlistmaker.domain.impl.TrackInteractorImpl

object Creator {

    private lateinit var application: Application

    fun initApplication(application: Application) {
        this.application = application
    }

    private fun getTracksRepository(): TrackRepository {
        return TracksRepositoryImpl(ItunesApiClient())
    }

    fun provideTracksInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTracksRepository())
    }

    fun provideSharedPreferencesInteractor(): SharedPreferencesInteractor =
        SharedPreferencesInteractorImpl(
            SharedPreferencesRepositoryImpl(
                application.getSharedPreferences("app_preferences", MODE_PRIVATE)
            )
        )

    fun providePlayerInteractor(): PlayerInteractor = PlayerInteractorlmpl()
}