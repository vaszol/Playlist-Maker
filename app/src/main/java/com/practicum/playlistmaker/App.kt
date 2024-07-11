package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.domain.Creator

class App : Application() {
    private var darkTheme = false
    private var sharedPreferences = Creator.provideSharedPreferencesInteractor()

    override fun onCreate() {
        super.onCreate()
        switchTheme(getThemePreferences())
    }

    fun getThemePreferences(): Boolean {
        return sharedPreferences.getThemePreferences(this, darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        sharedPreferences.switchTheme(this, darkTheme)
    }
}