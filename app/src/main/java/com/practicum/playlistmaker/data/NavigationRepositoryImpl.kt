package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.domain.NavigationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NavigationRepositoryImpl : NavigationRepository {
    private val _isBottomNavigationVisible = MutableStateFlow(true)
    override val isBottomNavigationVisible: StateFlow<Boolean> = _isBottomNavigationVisible

    override fun setBottomNavigationVisibility(isVisible: Boolean) {
        _isBottomNavigationVisible.value = isVisible
    }
}