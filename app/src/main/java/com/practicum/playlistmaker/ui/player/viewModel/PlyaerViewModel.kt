package com.practicum.playlistmaker.ui.player.viewModel

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.NavigationInteractor
import com.practicum.playlistmaker.domain.PlaylistInteractor
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.api.SharedPreferencesInteractor
import com.practicum.playlistmaker.domain.db.TracksDbInteractor
import com.practicum.playlistmaker.domain.models.PlayerStateEnum
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.player.PlayerScreenEvent
import com.practicum.playlistmaker.ui.player.PlayerScreenState
import com.practicum.playlistmaker.ui.search.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class PlyaerViewModel(
    private val tracksDbInteractor: TracksDbInteractor,
    private val creatorPlayer: PlayerInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val navigationInteractor: NavigationInteractor,
    sharedPreferencesInteractor: SharedPreferencesInteractor,
) : ViewModel() {

    private var mediaPlayer = MediaPlayer()
    private var timerJob: Job? = null
    var track: Track? = sharedPreferencesInteractor.getTrackToPlay()
    private val _state = MutableLiveData<PlayerScreenState>()
    val state: LiveData<PlayerScreenState> = _state
    private val _playlists = MutableLiveData<List<Playlist>>(listOf())
    val playlists: LiveData<List<Playlist>> = _playlists
    val event = SingleLiveEvent<PlayerScreenEvent>()

    init {
        subscribeOnLiked()
        subscribeOnPlaylist()
    }

    private fun subscribeOnLiked() {
        viewModelScope.launch(Dispatchers.IO) {
            tracksDbInteractor.getLikedTracks().collect { liked ->
                track?.let {
                    track = it.copy(isLiked = it.trackId in liked.map { t -> t.trackId }.toSet())
                }
                _state.postValue(getCurrentScreenState().copy(isLiked = track?.isLiked ?: false))
            }
        }
    }

    private fun subscribeOnPlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylists().collectLatest { _playlists.postValue(it) }
        }
    }

    fun setTrack() {
        _state.value =
            getCurrentScreenState().copy(track = track, isLiked = track?.isLiked ?: false)
        preparePlayer()
    }

    fun play() {
        creatorPlayer.playbackControl(mediaPlayer)
        if (mediaPlayer.isPlaying)
            _state.value = getCurrentScreenState().copy(playerState = PlayerStateEnum.STATE_PLAYING)
        else
            _state.value = getCurrentScreenState().copy(playerState = PlayerStateEnum.STATE_PAUSED)
        startTimer()
    }

    fun like() {
        viewModelScope.launch(Dispatchers.IO) {
            track?.apply {
                if (isLiked)
                    tracksDbInteractor.deleteFromLiked(this)
                else
                    tracksDbInteractor.addToLiked(this)
            }
        }
    }

    fun pause() {
        creatorPlayer.pausePlayer(mediaPlayer)
        _state.value = getCurrentScreenState().copy(playerState = PlayerStateEnum.STATE_PAUSED)
        timerJob?.cancel()
    }

    fun stop() {
        mediaPlayer.reset()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                _state.value =
                    getCurrentScreenState().copy(trackTime = getCurrentPlayerPosition())
                delay(TIMER_STEP_MILLIS)
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            ?: "00:00"
    }

    private fun preparePlayer() {
        track?.let {
            mediaPlayer.apply {
                setDataSource(it.previewUrl)
                prepareAsync()
                setOnPreparedListener {
                    _state.value =
                        getCurrentScreenState().copy(playerState = PlayerStateEnum.STATE_PAUSED)
                    creatorPlayer.playerState = PlayerStateEnum.STATE_PAUSED
                }
                setOnCompletionListener {
                    _state.value = getCurrentScreenState().copy(
                        playerState = PlayerStateEnum.STATE_PAUSED,
                        trackTime = ""
                    )
                    creatorPlayer.playerState = PlayerStateEnum.STATE_PAUSED
                }
            }
        }
    }

    private fun getCurrentScreenState() = _state.value ?: PlayerScreenState()

    fun add() {
        event.value = PlayerScreenEvent.OpenBottomSheet
    }

    companion object {
        private const val TIMER_STEP_MILLIS = 300L
    }

    fun onPlaylistClicked(playlist: Playlist) {
        track?.let {
            if (it.trackId !in playlist.tracksIds.toSet()) {
                viewModelScope.launch(Dispatchers.IO) {
                    playlistInteractor.addTrackToPlaylist(playlist, it)
                    withContext(Dispatchers.Main) {
                        event.value = PlayerScreenEvent.CloseBottomSheet
                        event.value = PlayerScreenEvent.ShowTrackAddedMessage(playlist.name)
                    }
                }
            } else {
                event.value = PlayerScreenEvent.ShowTrackAlreadyInPlaylistMessage(playlist.name)
            }
        }
    }

    fun onCreateClicker() {
        event.postValue(PlayerScreenEvent.NavigateToCreatePlaylistScreen)
    }

    fun hideNavigation() {
        navigationInteractor.setBottomNavigationVisibility(false)
    }
}