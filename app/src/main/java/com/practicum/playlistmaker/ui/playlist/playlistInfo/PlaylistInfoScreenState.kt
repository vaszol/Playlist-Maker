package com.practicum.playlistmaker.ui.playlist.playlistInfo

import com.practicum.playlistmaker.domain.models.Track

data class PlaylistInfoScreenState(
    val coverUri: String?,
    val name: String,
    val description: String,
    val tracksCount: Int,
    val minute: Int,
    val tracks: List<Track>
)