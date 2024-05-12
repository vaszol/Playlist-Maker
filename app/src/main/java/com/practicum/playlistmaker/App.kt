package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.itunes.ItunesResult

const val APP_PREFERENCES = "app_preferences"
const val DARK_THEME_KEY = "dark_theme_key"
const val SEARCH_HISTORY = "search_history_key"

class App : Application() {
    private var darkTheme = false
    private var sharedPrefs: SharedPreferences? = null

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        switchTheme(getThemePreferences())
    }

    fun getThemePreferences(): Boolean {
        val b = sharedPrefs?.getBoolean(DARK_THEME_KEY, darkTheme) == true
        return b
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPrefs?.edit()
            ?.putBoolean(DARK_THEME_KEY, darkTheme)
            ?.apply()
    }

    fun getFromHistory(): MutableList<ItunesResult> {
        val json = sharedPrefs?.getString(SEARCH_HISTORY, "")
        if (json.isNullOrEmpty()) {
            return mutableListOf()
        } else {
            val itemType = object : TypeToken<List<ItunesResult>>() {}.type
            return Gson().fromJson(json, itemType)
        }
    }

    fun addHistory(item: ItunesResult) {
        val history: MutableList<ItunesResult> = getFromHistory()
        if (history.isNotEmpty()) {
            if (history.any { it.trackId == item.trackId }) {
                history.remove(item)
            } else {
                while (history.size >= 10) history.removeFirst()
            }
        }
        history.add(item)
        sharedPrefs?.edit()
            ?.putString(SEARCH_HISTORY, Gson().toJson(history))
            ?.apply()
    }

    fun clearHistory() {
        sharedPrefs?.edit()
            ?.putString(SEARCH_HISTORY, "")
            ?.apply()
    }
}