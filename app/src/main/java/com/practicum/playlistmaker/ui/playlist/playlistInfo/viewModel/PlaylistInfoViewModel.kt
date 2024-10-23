package com.practicum.playlistmaker.ui.playlist.playlistInfo.viewModel

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistInfoViewModel(
    private val navigationInteractor: NavigationInteractor,
    private val sharedPreferencesInteractor: SharedPreferencesInteractor,
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    private val _playlistId = MutableStateFlow<String?>(null)
    val playlistId: StateFlow<String?> = _playlistId
    private val _playlist = MutableStateFlow<Playlist?>(null)
    val playlist: StateFlow<Playlist?> = _playlist
    private val _tracks = MutableStateFlow<List<Track>>(listOf())
    val tracks: StateFlow<List<Track>> = _tracks
    private val _state = MutableStateFlow<PlaylistInfoScreenState?>(null)
    val state: StateFlow<PlaylistInfoScreenState?> = _state
    val event = SingleLiveEvent<PlaylistInfoScreenEvent>()
    private var trackIdForDeletion: String? = null

    init {
        subscribeOnPlaylist()
        subscribeOnState()
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
            _playlistId.emit(sharedPreferencesInteractor.getPlaylistToInfo())
            playlistId.value?.let {
                playlistInteractor.getPlaylistById(it).onEach { updatedPlaylist ->
                    if (updatedPlaylist.tracksCount != playlist.value?.tracksCount) {
                        getPlaylistTracks(updatedPlaylist.tracksIds)
                    }
                    _playlist.update { updatedPlaylist }
                }.launchIn(viewModelScope)
            }
        }
    }

    private fun getPlaylistTracks(tracksIds: List<String>) {
        playlistInteractor.getTracksByIds(tracksIds).onEach { newTracks ->
            _tracks.update { newTracks }
        }.launchIn(viewModelScope)
    }

    private fun subscribeOnState() {
        combine(playlist, tracks) { playlist, tracks ->
            val state = PlaylistInfoScreenState(
                coverUri = playlist?.coverUri,
                name = playlist?.name ?: "",
                description = playlist?.description ?: "",
                minute = SimpleDateFormat("mm", Locale.getDefault())
                    .format(tracks.sumOf { track: Track -> track.trackTimeMillis }).toInt(),
                tracksCount = tracks.count(),
                emptyTracks = playlist?.tracksIds?.isEmpty() ?: false,
                tracks = tracks
            )
            _state.emit(state)
        }.launchIn(viewModelScope)
    }

    fun onTrackClick(track: Track) = clickDebounce(track)

    fun onLongClick(trackId: String) {
        trackIdForDeletion = trackId
        event.value = PlaylistInfoScreenEvent.ShowDeleteTrackConfirmationDialog
    }

    fun hideNavigation() {
        navigationInteractor.setBottomNavigationVisibility(false)
    }

    fun onDeleteTrackConfirmed() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistId.value.let { playlistId ->
                trackIdForDeletion?.let { trackIdForDeletion ->
                    playlistInteractor.deleteTrackFromPlaylist(
                        playlistId.toString(),
                        trackIdForDeletion
                    )
                }
            }
        }.invokeOnCompletion { trackIdForDeletion = null }
    }

    fun onDeleteTrackDialogDismiss() {
        trackIdForDeletion = null
    }

    fun onShareButtonClicked() {
        playlist.value?.let { playlist ->
            if (playlist.tracksIds.isEmpty())
                event.postValue(PlaylistInfoScreenEvent.ShowEmptyPlaylistDialog)
            else event.postValue(PlaylistInfoScreenEvent.SharePlaylist)
        }
    }

    fun onMenuButtonClicked() {
        event.postValue(PlaylistInfoScreenEvent.ShowMenu(true))
    }

    fun onBtnShareMenuClicked() {
        playlist.value?.let { playlist ->
            if (playlist.tracksIds.isEmpty()) {
                event.value = PlaylistInfoScreenEvent.ShowEmptyPlaylistDialog
                event.value = PlaylistInfoScreenEvent.ShowMenu(false)
            } else event.postValue(PlaylistInfoScreenEvent.SharePlaylist)
        }
    }

    fun onBtnEditMenuClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistId.collect {
                sharedPreferencesInteractor.setPlaylistToEdit(it.toString())
            }
        }
        event.value = PlaylistInfoScreenEvent.OpenEditPlaylistScreen
    }

    fun onBtnDeleteMenuClicked() {
        event.value = PlaylistInfoScreenEvent.ShowMenu(false)
        event.value = PlaylistInfoScreenEvent.ShowDeletePlaylistConfirmationDialog
    }

    fun onOverlayClicked() {
        event.postValue(PlaylistInfoScreenEvent.ShowMenu(false))
    }

    fun onDeletePlaylistDialogDismiss() {
        event.postValue(PlaylistInfoScreenEvent.ShowMenu(false))
    }

    fun onDeletePlaylistConfirmed() {
        playlistId.value?.let {
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