package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface SharedPreferencesInteractor {
    fun addHistory(listTrack: Track)

    fun getFromHistory(): MutableList<Track>

    fun clearSharedPreference()

    fun getThemePreferences(darkTheme: Boolean): Boolean

    fun switchTheme(darkTheme: Boolean)
}