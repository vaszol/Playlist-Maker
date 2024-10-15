package com.practicum.playlistmaker.domain

import kotlinx.coroutines.flow.StateFlow

interface NavigationInteractor {
    val isBottomNavigationVisible: StateFlow<Boolean>
    fun setBottomNavigationVisibility(isVisible: Boolean)
}