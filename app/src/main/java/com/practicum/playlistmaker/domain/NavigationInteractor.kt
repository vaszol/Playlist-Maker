package com.practicum.playlistmaker.domain

import androidx.lifecycle.LiveData

interface NavigationInteractor {
    val isBottomNavigationVisible: LiveData<Boolean>
    fun setBottomNavigationVisibility(isVisible: Boolean)
}