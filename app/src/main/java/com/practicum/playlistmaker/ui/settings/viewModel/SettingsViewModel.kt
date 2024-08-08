package com.practicum.playlistmaker.ui.settings.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.map
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.app.App

class SettingsViewModel(
    private val application: Application
) : ViewModel() {
    private val _theme = MutableLiveData((application as App).getThemePreferences())
    val theme: LiveData<Boolean> = _theme.map { it == true }
    fun themeSwitch(checked: Boolean) = (application as App).switchTheme(checked)

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }
}