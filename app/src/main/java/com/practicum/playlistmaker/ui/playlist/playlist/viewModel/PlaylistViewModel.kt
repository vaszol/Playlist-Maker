package com.practicum.playlistmaker.ui.playlist.playlist.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.NavigationInteractor
import com.practicum.playlistmaker.domain.PlaylistInteractor
import com.practicum.playlistmaker.domain.api.SharedPreferencesInteractor
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.ui.playlist.playlist.PlaylistsScreenEvent
import com.practicum.playlistmaker.ui.search.SingleLiveEvent
import com.practicum.playlistmaker.ui.util.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val navigationInteractor: NavigationInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val sharedPreferencesInteractor: SharedPreferencesInteractor,
) : ViewModel() {
    private val _playlist = MutableLiveData<List<Playlist>>(listOf())
    val playlist: LiveData<List<Playlist>> = _playlist
    val event = SingleLiveEvent<PlaylistsScreenEvent>()

    init {
        subscribeOnPlaylists()
    }

    private val clickDebounce =
        debounce<String>(CLICK_DEBOUNCE_DELAY, viewModelScope, false) { playlistId ->
            viewModelScope.launch(Dispatchers.IO) {
                sharedPreferencesInteractor.setPlaylistToInfo(playlistId)
            }
            navigationInteractor.setBottomNavigationVisibility(false)
            event.value = PlaylistsScreenEvent.NavigateToPlaylistInfo
        }

    fun onCreateBtnClick() {
        navigationInteractor.setBottomNavigationVisibility(false)
        event.value = PlaylistsScreenEvent.NavigateToCreatePlaylist
    }

    fun onPlaylistClicked(playlistId: String) = clickDebounce(playlistId)

    private fun subscribeOnPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylists().collect { _playlist.postValue(it) }
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 100L
    }
}