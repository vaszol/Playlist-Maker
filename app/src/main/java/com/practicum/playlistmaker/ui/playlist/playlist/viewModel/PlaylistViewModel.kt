package com.practicum.playlistmaker.ui.playlist.playlist.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.NavigationInteractor
import com.practicum.playlistmaker.domain.PlaylistInteractor
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.ui.playlist.playlist.PlaylistsScreenEvent
import com.practicum.playlistmaker.ui.search.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val navigationInteractor: NavigationInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private val _playlist = MutableLiveData<List<Playlist>>(listOf())
    val playlist: LiveData<List<Playlist>> = _playlist
    val event = SingleLiveEvent<PlaylistsScreenEvent>()

    init {
        subscribeOnPlaylists()
    }

    fun onCreateBtnClick() {
        navigationInteractor.setBottomNavigationVisibility(false)
        event.value = PlaylistsScreenEvent.NavigateToCreatePlaylist
    }

    fun onPlaylistClicked(playlistId: String) {
    }

    private fun subscribeOnPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylists().collect { _playlist.postValue(it) }
        }
    }
}