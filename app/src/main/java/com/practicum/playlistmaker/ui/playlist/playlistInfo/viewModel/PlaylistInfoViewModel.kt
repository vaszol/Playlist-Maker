package com.practicum.playlistmaker.ui.playlist.playlistInfo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.NavigationInteractor
import com.practicum.playlistmaker.domain.PlaylistInteractor
import com.practicum.playlistmaker.domain.api.SharedPreferencesInteractor
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.playlist.playlistInfo.PlaylistInfoScreenEvent
import com.practicum.playlistmaker.ui.playlist.playlistInfo.PlaylistInfoScreenState
import com.practicum.playlistmaker.ui.search.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistInfoViewModel(
    private val navigationInteractor: NavigationInteractor,
    sharedPreferencesInteractor: SharedPreferencesInteractor,
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    var playlistId: String? = sharedPreferencesInteractor.getPlaylistToPlay()
    private val _state = MutableLiveData<PlaylistInfoScreenState>()
    val state: LiveData<PlaylistInfoScreenState> = _state
    val event = SingleLiveEvent<PlaylistInfoScreenEvent>()

    init {
        subscribeOnPlaylist()
    }

    fun onBackPressed() {
        event.postValue(PlaylistInfoScreenEvent.NavigateBack)
        navigationInteractor.setBottomNavigationVisibility(true)
    }

    private fun subscribeOnPlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistId?.let {
                playlistInteractor.getPlaylistById(playlistId!!)
                    .collectLatest {
                        _state.postValue(
                            getCurrentScreenState().copy(playlist = it)
                        )
                        getPlaylistTracks(it)
                    }
            }
        }
    }

    private fun getPlaylistTracks(it: Playlist?) {
        viewModelScope.launch(Dispatchers.IO) {
            it?.tracksIds?.let { tracksIds ->
                playlistInteractor.getTracksByIds(tracksIds)
                    .collectLatest {
                        _state.postValue(
                            getCurrentScreenState().copy(
                                info = String.format(
                                    "%s минут · %s треков",
                                    SimpleDateFormat("mm", Locale.getDefault())
                                        .format(it.sumOf { track: Track -> track.trackTimeMillis }),
                                    it.size
                                )
                            )
                        )
                    }
            }
        }
    }

    private fun getCurrentScreenState() = _state.value ?: PlaylistInfoScreenState()
}