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
import com.practicum.playlistmaker.ui.util.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PlaylistInfoViewModel(
    private val navigationInteractor: NavigationInteractor,
    sharedPreferencesInteractor: SharedPreferencesInteractor,
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    var playlistId: String? = sharedPreferencesInteractor.getPlaylistToPlay()
    private val _state = MutableLiveData<PlaylistInfoScreenState>()
    val state: LiveData<PlaylistInfoScreenState> = _state
    val event = SingleLiveEvent<PlaylistInfoScreenEvent>()
    private var trackIdForDeletion: String? = null

    init {
        subscribeOnPlaylist()
    }

    private val clickDebounce =
        debounce<Track>(CLICK_DEBOUNCE_DELAY, viewModelScope, false) { track ->
            viewModelScope.launch(Dispatchers.IO) {
                sharedPreferencesInteractor.addHistory(track)
                sharedPreferencesInteractor.setTrackToPlay(track)
            }
            event.value = PlaylistInfoScreenEvent.OpenPlayerScreen
        }

    fun onBackPressed() {
        event.postValue(PlaylistInfoScreenEvent.NavigateBack)
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
                                tracks = it
                            )
                        )
                    }
            }
        }
    }

    private fun getCurrentScreenState() = _state.value ?: PlaylistInfoScreenState()

    fun onTrackClick(track: Track) = clickDebounce(track)

    fun onLongClick(trackId: String) {
        trackIdForDeletion = trackId
        event.value = PlaylistInfoScreenEvent.ShowDeleteTrackConfirmationDialog
    }

    fun hideNavigation() {
        navigationInteractor.setBottomNavigationVisibility(false)
    }

    fun onDeleteTrackConfirmed() {
        playlistId?.apply {
            trackIdForDeletion?.let {
                viewModelScope.launch(Dispatchers.IO) {
                    playlistInteractor.deleteTrackFromPlaylist(this@apply, it)
                }.invokeOnCompletion { trackIdForDeletion = null }
            }
        }
    }

    fun onDeleteTrackDialogDismiss() {
        trackIdForDeletion = null
    }

    fun onShareButtonClicked() {
        state.value?.playlist?.let { playlist ->
            if (playlist.tracksIds.isEmpty())
                event.postValue(PlaylistInfoScreenEvent.ShowEmptyPlaylistDialog)
            else event.postValue(PlaylistInfoScreenEvent.SharePlaylist)
        }
    }

    fun onMenuButtonClicked() {
        event.postValue(PlaylistInfoScreenEvent.ShowMenu(true))
    }

    fun onBtnShareMenuClicked() {
        event.postValue(PlaylistInfoScreenEvent.SharePlaylist)
    }

    fun onBtnEditMenuClicked() {
//        event.postValue(PlaylistInfoScreenEvent.ShowMenu(false))
    }

    fun onBtnDeleteMenuClicked() {
        event.postValue(PlaylistInfoScreenEvent.ShowDeletePlaylistConfirmationDialog)
    }

    fun onOverlayClicked() {
        event.postValue(PlaylistInfoScreenEvent.ShowMenu(false))
    }

    fun onDeletePlaylistDialogDismiss() {
        event.postValue(PlaylistInfoScreenEvent.ShowMenu(false))
    }

    fun onDeletePlaylistConfirmed() {
        playlistId?.let {
            viewModelScope.launch(Dispatchers.IO) {
                playlistInteractor.deletePlaylist(it)
            }
        }
        event.postValue(PlaylistInfoScreenEvent.NavigateBack)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 100L
    }
}