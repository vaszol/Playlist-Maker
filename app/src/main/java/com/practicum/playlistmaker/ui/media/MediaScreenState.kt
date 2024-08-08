package com.practicum.playlistmaker.ui.media

import com.practicum.playlistmaker.domain.models.PlayerStateEnum
import com.practicum.playlistmaker.domain.models.Track

data class MediaScreenState(
    val playerState: PlayerStateEnum = PlayerStateEnum.STATE_PAUSED,
    val trackTime: String = "",
    val track: Track? = null,
)