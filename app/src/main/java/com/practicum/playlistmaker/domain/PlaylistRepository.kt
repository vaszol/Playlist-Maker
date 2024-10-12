package com.practicum.playlistmaker.domain

import com.practicum.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist)
    fun getPlaylistsFlow(): Flow<List<Playlist>>
}