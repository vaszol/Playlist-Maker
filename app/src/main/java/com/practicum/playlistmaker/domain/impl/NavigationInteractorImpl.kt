package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.NavigationInteractor
import com.practicum.playlistmaker.domain.NavigationRepository
import kotlinx.coroutines.flow.StateFlow

class NavigationInteractorImpl(
    private val navigationRepository: NavigationRepository
) : NavigationInteractor {
    override val isBottomNavigationVisible: StateFlow<Boolean> =
        navigationRepository.isBottomNavigationVisible

    override fun setBottomNavigationVisibility(isVisible: Boolean) {
        navigationRepository.setBottomNavigationVisibility(isVisible)
    }
}