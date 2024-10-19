package com.practicum.playlistmaker.ui.playlist.playlistInfo

import com.practicum.playlistmaker.domain.models.Playlist

data class PlaylistInfoScreenState(
    val playlist: Playlist? = null,
    val info: String? = null,
)