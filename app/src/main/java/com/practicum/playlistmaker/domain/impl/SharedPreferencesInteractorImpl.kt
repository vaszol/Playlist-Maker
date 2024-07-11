package com.practicum.playlistmaker.domain.impl

import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.domain.api.SharedPreferencesInteractor
import com.practicum.playlistmaker.domain.api.SharedPreferencesRepository
import com.practicum.playlistmaker.domain.models.Track

class SharedPreferencesInteractorImpl(private val repo: SharedPreferencesRepository) :
    SharedPreferencesInteractor {

    override fun addHistory(context: Context, listTrack: Track) {
        repo.addHistory(context, listTrack)
    }

    override fun getFromHistory(context: Context): MutableList<Track> {
        return repo.getFromHistory(context)
    }

    override fun getSharedPreferences(context: Context): SharedPreferences {
        return getSharedPreferences(context)
    }

    override fun clearSharedPreference(context: Context) {
        repo.clearSharedPreference(context)
    }

    override fun getThemePreferences(context: Context, darkTheme: Boolean): Boolean {
        return repo.getThemePreferences(context, darkTheme)
    }

    override fun switchTheme(context: Context, darkTheme: Boolean) {
        repo.switchTheme(context, darkTheme)
    }
}