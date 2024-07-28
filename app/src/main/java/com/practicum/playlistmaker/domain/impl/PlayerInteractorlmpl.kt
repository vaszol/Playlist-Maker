package com.practicum.playlistmaker.domain.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.models.PlayerStateEnum

class PlayerInteractorlmpl : PlayerInteractor {
    private var _playerState: PlayerStateEnum? = null

    override var playerState: PlayerStateEnum?
        get() = _playerState
        set(value) {
            _playerState = value
        }

    override fun pausePlayer(mediaPlayer: MediaPlayer) {
        mediaPlayer.pause()
        playerState = PlayerStateEnum.STATE_PAUSED
    }

    override fun startPlayer(mediaPlayer: MediaPlayer) {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            playerState = PlayerStateEnum.STATE_PLAYING
        }
    }

    override fun playbackControl(mediaPlayer: MediaPlayer) {
        when (playerState) {
            PlayerStateEnum.STATE_PLAYING -> pausePlayer(mediaPlayer)
            PlayerStateEnum.STATE_PAUSED -> startPlayer(mediaPlayer)
            null -> {}
        }
    }
}