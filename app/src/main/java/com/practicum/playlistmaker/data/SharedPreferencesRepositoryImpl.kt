package com.practicum.playlistmaker.data

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.api.SharedPreferencesRepository
import com.practicum.playlistmaker.domain.models.Track

class SharedPreferencesRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : SharedPreferencesRepository {

    @SuppressLint("CommitPrefEdits")
    override fun addHistory(track: Track) {
        val history: MutableList<Track> = getFromHistory()
        if (history.isNotEmpty()) {
            if (history.any { it.trackId == track.trackId }) {
                history.remove(track)
            } else {
                while (history.size >= 10) history.removeFirst()
            }
        }
        history.add(track)
        sharedPreferences.edit { putString(SEARCH_HISTORY, Gson().toJson(history)) }
    }

    override fun getFromHistory(): MutableList<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY, "")
        if (json.isNullOrEmpty()) {
            return mutableListOf()
        } else {
            val itemType = object : TypeToken<List<Track>>() {}.type
            return Gson().fromJson(json, itemType)
        }
    }

    @SuppressLint("CommitPrefEdits")
    override fun clearSharedPreference() {
        sharedPreferences.edit { putString(SEARCH_HISTORY, "") }
    }

    override fun getThemePreferences(darkTheme: Boolean): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME_KEY, darkTheme)
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

    companion object {
        private const val SEARCH_HISTORY = "search_history_key"
        private const val DARK_THEME_KEY = "dark_theme_key"
    }
}