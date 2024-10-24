package com.practicum.playlistmaker.ui.playlist.create

import com.practicum.playlistmaker.domain.models.Playlist

data class CreateScreenState(
    val playlist: Playlist? = null,
)