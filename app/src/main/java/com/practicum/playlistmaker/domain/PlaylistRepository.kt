package com.practicum.playlistmaker.domain

import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist)
    fun getPlaylistsFlow(): Flow<List<Playlist>>
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)
    fun getPlaylist(playlistId: String): Flow<Playlist?>
    fun getTracksByIds(tracksIds: List<String>): Flow<List<Track>>
}