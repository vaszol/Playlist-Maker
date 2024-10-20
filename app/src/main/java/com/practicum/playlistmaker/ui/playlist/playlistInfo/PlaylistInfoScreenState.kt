package com.practicum.playlistmaker.ui.playlist.playlistInfo

import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track

data class PlaylistInfoScreenState(
    val playlist: Playlist? = null,
    val info: String? = null,
    val tracks: List<Track> = listOf(),
)