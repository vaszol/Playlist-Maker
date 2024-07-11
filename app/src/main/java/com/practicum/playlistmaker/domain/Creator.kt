package com.practicum.playlistmaker.domain

import com.practicum.playlistmaker.data.ImageRepositoryImpl
import com.practicum.playlistmaker.data.SharedPreferencesRepositoryImpl
import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.ItunesApiClient
import com.practicum.playlistmaker.domain.api.ImageInteractor
import com.practicum.playlistmaker.domain.api.IntentActionInteractor
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.api.SharedPreferencesInteractor
import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.impl.ImageInteractorlmpl
import com.practicum.playlistmaker.domain.impl.IntentActionInteractorlmpl
import com.practicum.playlistmaker.domain.impl.PlayerInteractorlmpl
import com.practicum.playlistmaker.domain.impl.SharedPreferencesInteractorImpl
import com.practicum.playlistmaker.domain.impl.TrackInteractorImpl

object Creator {
    private fun getTracksRepository(): TrackRepository {
        return TracksRepositoryImpl(ItunesApiClient())
    }

    fun provideTracksInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTracksRepository())
    }

    fun provideSharedPreferencesInteractor(): SharedPreferencesInteractor =
        SharedPreferencesInteractorImpl(
            SharedPreferencesRepositoryImpl()
        )

    fun providePlayerInteractor(): PlayerInteractor = PlayerInteractorlmpl()

    fun provideImageInteractor(): ImageInteractor {
        return ImageInteractorlmpl(ImageRepositoryImpl())
    }

    fun provideIntentActionInteractor(): IntentActionInteractor = IntentActionInteractorlmpl()
}