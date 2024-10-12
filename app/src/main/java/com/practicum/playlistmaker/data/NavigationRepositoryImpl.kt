package com.practicum.playlistmaker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.domain.NavigationRepository

class NavigationRepositoryImpl : NavigationRepository {
    private val _isBottomNavigationVisible = MutableLiveData<Boolean>(true)
    override val isBottomNavigationVisible: LiveData<Boolean> = _isBottomNavigationVisible

    override fun setBottomNavigationVisibility(isVisible: Boolean) {
        _isBottomNavigationVisible.value = isVisible
    }
}