package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.SharedPreferencesInteractor
import com.practicum.playlistmaker.domain.api.SharedPreferencesRepository
import com.practicum.playlistmaker.domain.models.Track

class SharedPreferencesInteractorImpl(private val repo: SharedPreferencesRepository) :
    SharedPreferencesInteractor {

    override fun addHistory(listTrack: Track) {
        repo.addHistory(listTrack)
    }

    override fun getFromHistory(): MutableList<Track> {
        return repo.getFromHistory()
    }

    override fun clearSharedPreference() {
        repo.clearSharedPreference()
    }

    override fun getThemePreferences(darkTheme: Boolean): Boolean {
        return repo.getThemePreferences(darkTheme)
    }

    override fun switchTheme(darkTheme: Boolean) {
        repo.switchTheme(darkTheme)
    }
}