package com.practicum.playlistmaker.domain.api

import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.domain.models.Track

interface SharedPreferencesRepository {
    fun addHistory(context: Context, track: Track)

    fun getFromHistory(context: Context): MutableList<Track>

    fun getSharedPreferences(context: Context): SharedPreferences

    fun clearSharedPreference(context: Context)

    fun getThemePreferences(context: Context, darkTheme: Boolean): Boolean

    fun switchTheme(context: Context, darkTheme: Boolean)
}