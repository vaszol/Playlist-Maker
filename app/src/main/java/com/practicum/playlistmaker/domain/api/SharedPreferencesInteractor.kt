package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface SharedPreferencesInteractor {
    fun addHistory(track: Track)

    fun getFromHistory(): MutableList<Track>

    fun setTrackToPlay(track: Track)

    fun getTrackToPlay(): Track?

    fun clearSharedPreference()

    fun getThemePreferences(): Boolean

    fun switchTheme(darkTheme: Boolean)
}