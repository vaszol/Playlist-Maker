package com.practicum.playlistmaker.ui.settings.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.practicum.playlistmaker.domain.api.SharedPreferencesInteractor

class SettingsViewModel(
    private var sharedPreferences: SharedPreferencesInteractor
) : ViewModel() {
    private val _theme = MutableLiveData(sharedPreferences.getThemePreferences())
    val theme: LiveData<Boolean> = _theme.map { it == true }

    fun themeSwitch(checked: Boolean) {
        sharedPreferences.switchTheme(checked)
        _theme.value = sharedPreferences.getThemePreferences()
    }
}