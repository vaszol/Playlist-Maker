package com.practicum.playlistmaker.ui.playlist.favorite.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.models.Track

class FavoriteViewModel : ViewModel() {
    private val _tracks = MutableLiveData<List<Track>>(listOf())
    val tracks: LiveData<List<Track>> = _tracks
}