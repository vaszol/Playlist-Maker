package com.practicum.playlistmaker.app

import android.app.Application
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.api.SharedPreferencesInteractor

class App : Application() {
    private var darkTheme = false
    private var sharedPreferences: SharedPreferencesInteractor? = null

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        sharedPreferences = Creator.provideSharedPreferencesInteractor()
        switchTheme(getThemePreferences())
    }

    fun getThemePreferences(): Boolean {
        return sharedPreferences?.getThemePreferences(darkTheme) ?: darkTheme
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        if (sharedPreferences != null) sharedPreferences!!.switchTheme(darkTheme)
    }
}