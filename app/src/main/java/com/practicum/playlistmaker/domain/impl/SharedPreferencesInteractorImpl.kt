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
        return repo.getFromHistory().reversed().toMutableList()
    }

    override fun setTrackToPlay(listTrack: Track) {
        repo.setTrackToPlay(listTrack)
    }

    override fun getTrackToPlay(): Track? {
        return repo.getTrackToPlay()
    }

    override fun clearSharedPreference() {
        repo.clearSharedPreference()
    }

    override fun getThemePreferences(): Boolean {
        return repo.getThemePreferences()
    }

    override fun switchTheme(darkTheme: Boolean) {
        repo.switchTheme(darkTheme)
    }
}