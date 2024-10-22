package com.practicum.playlistmaker.domain

import android.net.Uri
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun addPlayList(name: String, description: String?, uri: Uri?)
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)
    fun getPlaylistById(playlistId: String): Flow<Playlist>
    fun getTracksByIds(tracksIds: List<String>): Flow<List<Track>>
    suspend fun deleteTrackFromPlaylist(playlistId: String, trackId: String)
    suspend fun deletePlaylist(playlistId: String)
    suspend fun updatePlaylist(playlist: Playlist, name: String, description: String?, uri: Uri?)
}