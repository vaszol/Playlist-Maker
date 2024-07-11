package com.practicum.playlistmaker.domain.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.models.PlayerStateEnum

class PlayerInteractorlmpl : PlayerInteractor {
    private var _playerState: PlayerStateEnum? = null

    override var playerState: PlayerStateEnum?
        get() = _playerState
        set(value) {
            _playerState = value
        }

    override fun pausePlayer(mediaPlayer: MediaPlayer, binding: ActivityMediaBinding) {
        mediaPlayer.pause()
        binding.playBtn.setImageResource(R.drawable.ic_play)
        playerState = PlayerStateEnum.STATE_PAUSED
    }

    override fun startPlayer(mediaPlayer: MediaPlayer, binding: ActivityMediaBinding) {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            binding.playBtn.setImageResource(R.drawable.ic_pause)
            playerState = PlayerStateEnum.STATE_PLAYING
        }
    }

    override fun playbackControl(mediaPlayer: MediaPlayer, binding: ActivityMediaBinding) {
        when (playerState) {
            PlayerStateEnum.STATE_PLAYING -> pausePlayer(mediaPlayer, binding)
            PlayerStateEnum.STATE_PREPARED, PlayerStateEnum.STATE_PAUSED -> startPlayer(
                mediaPlayer,
                binding
            )

            PlayerStateEnum.STATE_DEFAULT -> {}
            null -> {}
        }
    }
}