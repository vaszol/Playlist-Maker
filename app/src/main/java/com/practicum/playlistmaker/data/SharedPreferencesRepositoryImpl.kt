package com.practicum.playlistmaker.data

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.domain.api.SharedPreferencesRepository
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.SharedPreferencesConverter

class SharedPreferencesRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val sharedPreferencesConverter: SharedPreferencesConverter,
    private val appDatabase: AppDatabase
) : SharedPreferencesRepository {

    private var track: Track? = null
    private var infoPlaylistId: String? = null
    private var editPlaylistId: String? = null

    init {
        switchTheme(getThemePreferences())
    }

    @SuppressLint("CommitPrefEdits")
    override fun addHistory(track: Track) {
        val history: MutableList<Track> = getFromHistory().toMutableList()
        if (history.isNotEmpty()) {
            if (history.any { it.trackId == track.trackId }) {
                history.removeIf { historyTrack -> historyTrack.trackId == track.trackId }
            } else {
                while (history.size >= 10) history.removeAt(0)
            }
        }
        history.add(track)
        val tracksString = sharedPreferencesConverter.convertListToJson(history)
        sharedPreferences.edit { putString(SEARCH_HISTORY, tracksString) }
    }

    override fun getFromHistory(): MutableList<Track> {
        val likedIds = appDatabase.likedTrackDao().getTracksIds().toSet()
        return sharedPreferences.getString(SEARCH_HISTORY, null)?.let {
            sharedPreferencesConverter.convertJsonToList(it)
                .map { it.copy(isLiked = it.trackId in likedIds) }
        }?.toMutableList() ?: mutableListOf()
    }

    override fun setTrackToPlay(track: Track) {
        this.track = track
    }

    override fun getTrackToPlay(): Track? {
        return track.let {
            val currentTrack = track?.copy()
            track = null
            currentTrack
        }
    }

    @SuppressLint("CommitPrefEdits")
    override fun clearSharedPreference() {
        sharedPreferences.edit { putString(SEARCH_HISTORY, "") }
    }

    override fun getThemePreferences(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME_KEY, false)
    }

    @SuppressLint("CommitPrefEdits")
    override fun switchTheme(darkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPreferences.edit { putBoolean(DARK_THEME_KEY, darkTheme) }
    }

    override fun setPlaylistToInfo(playlistId: String) {
        this.infoPlaylistId = playlistId
    }

    override fun getPlaylistToInfo(): String? {
        return infoPlaylistId.let {
            val currentId = infoPlaylistId
            infoPlaylistId = null
            currentId
        }
    }

    override fun setPlaylistToEdit(playlistId: String) {
        this.editPlaylistId = playlistId
    }

    override fun getPlaylistToEdit(): String? {
        return editPlaylistId.let {
            val currentId = editPlaylistId
            editPlaylistId = null
            currentId
        }
    }

    companion object {
        private const val SEARCH_HISTORY = "search_history_key"
        private const val DARK_THEME_KEY = "dark_theme_key"
    }
}