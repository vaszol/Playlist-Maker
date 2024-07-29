package com.practicum.playlistmaker.domain.api

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.models.PlayerStateEnum

interface PlayerInteractor {
    var playerState: PlayerStateEnum?
    fun pausePlayer(mediaPlayer: MediaPlayer)
    fun startPlayer(mediaPlayer: MediaPlayer)
    fun playbackControl(mediaPlayer: MediaPlayer)
}