package com.practicum.playlistmaker.ui.media.viewModel

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.api.SharedPreferencesInteractor
import com.practicum.playlistmaker.domain.models.PlayerStateEnum
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.media.MediaScreenState
import java.text.SimpleDateFormat
import java.util.Locale

class MediaViewModel(
    private val creatorPlayer: PlayerInteractor,
    sharedPreferencesInteractor: SharedPreferencesInteractor,
) : ViewModel() {

    private var mediaPlayer = MediaPlayer()
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private var track: Track? = sharedPreferencesInteractor.getTrackToPlay()
    private val _state = MutableLiveData<MediaScreenState>()
    val state: LiveData<MediaScreenState> = _state

    private val mediaRunnable = object : Runnable {
        override fun run() {
            if (mediaPlayer.isPlaying) {
                _state.postValue(
                    MediaScreenState(
                        PlayerStateEnum.STATE_PLAYING,
                        SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(mediaPlayer.currentPosition)
                    )
                )
                mainThreadHandler.postDelayed(this, 1000L)
            } else {
                pause()
            }
        }
    }

    fun setTrack() {
        _state.postValue(MediaScreenState(track = track))
        preparePlayer()
    }

    fun play() {
        creatorPlayer.playbackControl(mediaPlayer)
        if (mediaPlayer.isPlaying)
            _state.value = getCurrentScreenState().copy(playerState = PlayerStateEnum.STATE_PLAYING)
        else
            _state.value = getCurrentScreenState().copy(playerState = PlayerStateEnum.STATE_PAUSED)
        mainThreadHandler.post(mediaRunnable)
    }

    fun pause() {
        creatorPlayer.pausePlayer(mediaPlayer)
        _state.value = getCurrentScreenState().copy(playerState = PlayerStateEnum.STATE_PAUSED)
        mainThreadHandler.removeCallbacks(mediaRunnable)
    }

    fun stop() {
        mediaPlayer.reset()
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