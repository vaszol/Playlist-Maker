package com.practicum.playlistmaker.ui.settings.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.practicum.playlistmaker.domain.api.SharedPreferencesInteractor

class SettingsViewModel(
    private var sharedPreferences: SharedPreferencesInteractor
) : ViewModel() {
    val theme: LiveData<Boolean> =
        MutableLiveData(sharedPreferences.getThemePreferences()).map { it == true }

    fun themeSwitch(checked: Boolean) {
        sharedPreferences.switchTheme(checked)
    }
}