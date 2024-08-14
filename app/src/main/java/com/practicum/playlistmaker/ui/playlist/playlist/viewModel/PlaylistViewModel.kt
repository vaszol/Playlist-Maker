package com.practicum.playlistmaker.ui.playlist.playlist.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.models.Playlist

class PlaylistViewModel : ViewModel() {
    private val _playlist = MutableLiveData<List<Playlist>>(listOf())
    val playlist: LiveData<List<Playlist>> = _playlist
}