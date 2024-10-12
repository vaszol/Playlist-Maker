package com.practicum.playlistmaker.domain

import androidx.lifecycle.LiveData

interface NavigationRepository {
    val isBottomNavigationVisible: LiveData<Boolean>
    fun setBottomNavigationVisibility(isVisible: Boolean)
}