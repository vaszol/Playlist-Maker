package com.practicum.playlistmaker.ui.playlist.favorite.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.NavigationInteractor
import com.practicum.playlistmaker.domain.api.SharedPreferencesInteractor
import com.practicum.playlistmaker.domain.db.TracksDbInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.playlist.favorite.FavoriteScreenEvent
import com.practicum.playlistmaker.ui.search.SingleLiveEvent
import com.practicum.playlistmaker.ui.util.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val tracksDbInteractor: TracksDbInteractor,
    private val sharedPreferencesInteractor: SharedPreferencesInteractor,
    private val navigationInteractor: NavigationInteractor,
) : ViewModel() {

    private val _tracks = MutableLiveData<List<Track>>(listOf())
    val tracks: LiveData<List<Track>> = _tracks

    init {
        viewModelScope.launch(Dispatchers.IO) {
            tracksDbInteractor.getLikedTracks().collect { _tracks.postValue(it) }
        }
    }

    private val clickDebounce =
        debounce<Track>(CLICK_DEBOUNCE_DELAY, viewModelScope, false) { track ->
            viewModelScope.launch(Dispatchers.IO) {
                sharedPreferencesInteractor.addHistory(track)
                sharedPreferencesInteractor.setTrackToPlay(track)
            }
            event.value = FavoriteScreenEvent.OpenPlayerScreen
        }
    val event = SingleLiveEvent<FavoriteScreenEvent>()

    fun onTrackClick(track: Track) = clickDebounce(track)

    fun showNavigation() {
        navigationInteractor.setBottomNavigationVisibility(true)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 100L
    }
}