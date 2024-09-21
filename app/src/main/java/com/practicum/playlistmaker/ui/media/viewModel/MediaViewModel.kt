package com.practicum.playlistmaker.ui.media.viewModel

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.api.SharedPreferencesInteractor
import com.practicum.playlistmaker.domain.db.TracksDbInteractor
import com.practicum.playlistmaker.domain.models.PlayerStateEnum
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.media.MediaScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MediaViewModel(
    private val tracksDbInteractor: TracksDbInteractor,
    private val creatorPlayer: PlayerInteractor,
    sharedPreferencesInteractor: SharedPreferencesInteractor,
) : ViewModel() {

    companion object {
        private const val TIMER_STEP_MILLIS = 300L
    }

    private var mediaPlayer = MediaPlayer()
    private var timerJob: Job? = null
    private var track: Track? = sharedPreferencesInteractor.getTrackToPlay()
    private val _state = MutableLiveData<MediaScreenState>()
    val state: LiveData<MediaScreenState> = _state

    init {
        subscribeOnLiked()
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

    fun setTrack() {
        _state.postValue(MediaScreenState(track = track, isLiked = track?.isLiked ?: false))
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

    private fun getCurrentScreenState() = _state.value ?: MediaScreenState()
}