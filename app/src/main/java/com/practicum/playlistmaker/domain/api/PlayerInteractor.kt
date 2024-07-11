package com.practicum.playlistmaker.domain.api

import android.media.MediaPlayer
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.domain.models.PlayerStateEnum

interface PlayerInteractor {
    var playerState: PlayerStateEnum?
    fun pausePlayer(mediaPlayer: MediaPlayer, binding: ActivityMediaBinding)

    fun startPlayer(mediaPlayer: MediaPlayer, binding: ActivityMediaBinding)

    fun playbackControl(mediaPlayer: MediaPlayer, binding: ActivityMediaBinding)
}