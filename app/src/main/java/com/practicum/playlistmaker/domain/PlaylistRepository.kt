package com.practicum.playlistmaker.domain

import com.practicum.playlistmaker.domain.models.Playlist

interface PlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist)
}