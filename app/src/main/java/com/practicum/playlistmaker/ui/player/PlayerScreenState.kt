package com.practicum.playlistmaker.ui.player

import com.practicum.playlistmaker.domain.models.PlayerStateEnum
import com.practicum.playlistmaker.domain.models.Track

data class PlayerScreenState(
    val playerState: PlayerStateEnum = PlayerStateEnum.STATE_PAUSED,
    val trackTime: String = "",
    val track: Track? = null,
    val isLiked: Boolean = false,
)