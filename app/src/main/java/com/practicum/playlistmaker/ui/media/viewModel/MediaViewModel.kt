package com.practicum.playlistmaker.ui.media.viewModel

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.models.PlayerStateEnum
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.media.MediaScreenState
import java.text.SimpleDateFormat
import java.util.Locale

class MediaViewModel : ViewModel() {

    private var mediaPlayer = MediaPlayer()
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private var creatorPlayer = Creator.providePlayerInteractor()
    private var track: Track? = null
    private val _state = MutableLiveData<MediaScreenState>()
    val state: LiveData<MediaScreenState> = _state
    private val mediaRunnable = object : Runnable {
        override fun run() {
            if (mediaPlayer.isPlaying) {
                _state.value =
                    getCurrentScreenState().copy(
                        playerState = PlayerStateEnum.STATE_PLAYING,
                        trackTime = SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(mediaPlayer.currentPosition)
                    )
                mainThreadHandler.postDelayed(this, 1000L)
            } else {
                creatorPlayer.pausePlayer(mediaPlayer)
                _state.postValue(MediaScreenState(PlayerStateEnum.STATE_PAUSED))
            }
        }
    }

    fun setTrack(track: Track?) {
        _state.postValue(MediaScreenState(track = track))
        preparePlayer(track!!.previewUrl)
        this.track = track
    }

    fun play() {
        creatorPlayer.playbackControl(mediaPlayer)
        if (mediaPlayer.isPlaying)
            _state.postValue(MediaScreenState(PlayerStateEnum.STATE_PLAYING))
        else
            _state.postValue(MediaScreenState(PlayerStateEnum.STATE_PAUSED))
        mainThreadHandler.post(mediaRunnable)
    }

    fun pause() {
        creatorPlayer.pausePlayer(mediaPlayer)
        _state.postValue(MediaScreenState(PlayerStateEnum.STATE_PAUSED))
        mainThreadHandler.removeCallbacks(mediaRunnable)
    }

    fun stop() {
        mediaPlayer.release()
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            _state.postValue(MediaScreenState(PlayerStateEnum.STATE_PAUSED))
            creatorPlayer.playerState = PlayerStateEnum.STATE_PAUSED
        }
        mediaPlayer.setOnCompletionListener {
            _state.postValue(MediaScreenState(PlayerStateEnum.STATE_PAUSED))
            creatorPlayer.playerState = PlayerStateEnum.STATE_PAUSED
        }
    }

    private fun getCurrentScreenState() = _state.value ?: MediaScreenState()
}