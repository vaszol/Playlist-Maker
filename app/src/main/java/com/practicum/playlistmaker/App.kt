package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val APP_PREFERENCES = "app_preferences"
const val DARK_THEME_KEY = "dark_theme_key"

class App : Application() {
    var darkTheme = false
    var sharedPrefs: SharedPreferences? = null

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
}