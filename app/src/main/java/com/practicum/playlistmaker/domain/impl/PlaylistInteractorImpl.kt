package com.practicum.playlistmaker.domain.impl

import android.net.Uri
import com.practicum.playlistmaker.domain.ExternalStorageRepository
import com.practicum.playlistmaker.domain.PlaylistInteractor
import com.practicum.playlistmaker.domain.PlaylistRepository
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class PlaylistInteractorImpl(
    private val externalStorageRepository: ExternalStorageRepository,
    private val playlistRepository: PlaylistRepository,
) : PlaylistInteractor {
    override suspend fun addPlayList(name: String, description: String?, value: Uri?) {
        val id = UUID.randomUUID().toString()
        val playlistCoverUri = value?.let {
            externalStorageRepository.savePlaylistCover(id, value)
        }
        val playlist = Playlist(
            id = id,
            name = name,
            description = description,
            coverUri = playlistCoverUri
        )
        playlistRepository.addPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> = playlistRepository.getPlaylistsFlow()

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        playlistRepository.addTrackToPlaylist(playlist, track)
    }
}