package com.practicum.playlistmaker.domain.impl

import androidx.lifecycle.LiveData
import com.practicum.playlistmaker.domain.NavigationInteractor
import com.practicum.playlistmaker.domain.NavigationRepository

class NavigationInteractorImpl(
    private val navigationRepository: NavigationRepository
) : NavigationInteractor {
    override val isBottomNavigationVisible: LiveData<Boolean> =
        navigationRepository.isBottomNavigationVisible

    override fun setBottomNavigationVisibility(isVisible: Boolean) {
        navigationRepository.setBottomNavigationVisibility(isVisible)
    }
}