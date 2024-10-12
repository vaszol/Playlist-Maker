package com.practicum.playlistmaker.ui.playlist.playlist.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.NavigationInteractor
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.ui.playlist.playlist.PlaylistsScreenEvent
import com.practicum.playlistmaker.ui.search.SingleLiveEvent

class PlaylistViewModel(
    private val navigationInteractor: NavigationInteractor
) : ViewModel() {
    private val _playlist = MutableLiveData<List<Playlist>>(listOf())
    val playlist: LiveData<List<Playlist>> = _playlist
    val event = SingleLiveEvent<PlaylistsScreenEvent>()

    fun onCreateBtnClick() {
        navigationInteractor.setBottomNavigationVisibility(false)
        event.value = PlaylistsScreenEvent.NavigateToCreatePlaylist
    }
}