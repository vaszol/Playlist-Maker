package com.practicum.playlistmaker.ui.main

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.NavigationInteractor

class MainViewModel(navigationInteractor: NavigationInteractor):ViewModel() {
    val isBottomNavigationVisible = navigationInteractor.isBottomNavigationVisible
}