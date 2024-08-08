package com.practicum.playlistmaker.creator

import android.app.Application
import android.app.Application.MODE_PRIVATE
import android.content.Context
import com.google.gson.Gson
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
        Creator.application = application
    }

    private fun getTracksRepository(context: Context): TrackRepository {
        return TracksRepositoryImpl(ItunesApiClient(context))
    }

    fun provideTracksInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(getTracksRepository(context))
    }

    fun provideSharedPreferencesInteractor(): SharedPreferencesInteractor =
        SharedPreferencesInteractorImpl(
            SharedPreferencesRepositoryImpl(
                application.getSharedPreferences("app_preferences", MODE_PRIVATE)
            )
        )

    fun providePlayerInteractor(): PlayerInteractor = PlayerInteractorlmpl()

    fun getGson(): Gson {
        return Gson()
    }
}