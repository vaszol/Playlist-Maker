package com.practicum.playlistmaker.data

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.api.SharedPreferencesRepository
import com.practicum.playlistmaker.domain.models.Track

class SharedPreferencesRepositoryImpl : SharedPreferencesRepository {

    override fun addHistory(context: Context, track: Track) {
        val history: MutableList<Track> = getFromHistory(context)
        if (history.isNotEmpty()) {
            if (history.any { it.trackId == track.trackId }) {
                history.remove(track)
            } else {
                while (history.size >= 10) history.removeFirst()
            }
        }
        history.add(track)
        getSharedPreferences(context).edit()
            .putString(SEARCH_HISTORY, Gson().toJson(history))
            .apply()
    }

    override fun getFromHistory(context: Context): MutableList<Track> {
        val json = getSharedPreferences(context).getString(SEARCH_HISTORY, "")
        if (json.isNullOrEmpty()) {
            return mutableListOf()
        } else {
            val itemType = object : TypeToken<List<Track>>() {}.type
            return Gson().fromJson(json, itemType)
        }
    }

    override fun getSharedPreferences(context: Context): SharedPreferences {
        val prefData: SharedPreferences = context.getSharedPreferences(
            APP_PREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
        return prefData
    }

    override fun clearSharedPreference(context: Context) {
        getSharedPreferences(context).edit().putString(SEARCH_HISTORY, "").apply()
    }

    override fun getThemePreferences(context: Context, darkTheme: Boolean): Boolean {
        return getSharedPreferences(context).getBoolean(DARK_THEME_KEY, darkTheme)
    }

    override fun switchTheme(context: Context, darkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        getSharedPreferences(context).edit().putBoolean(DARK_THEME_KEY, darkTheme).apply()
    }

    companion object {
        private const val APP_PREFERENCES = "app_preferences"
        private const val SEARCH_HISTORY = "search_history_key"
        private const val DARK_THEME_KEY = "dark_theme_key"
    }
}