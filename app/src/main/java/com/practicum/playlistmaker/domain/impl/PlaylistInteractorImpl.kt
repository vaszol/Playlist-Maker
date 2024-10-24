package com.practicum.playlistmaker.domain.impl

import android.net.Uri
import com.practicum.playlistmaker.domain.ExternalStorageRepository
import com.practicum.playlistmaker.domain.PlaylistInteractor
import com.practicum.playlistmaker.domain.PlaylistRepository
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import java.util.UUID

class PlaylistInteractorImpl(
    private val externalStorageRepository: ExternalStorageRepository,
    private val playlistRepository: PlaylistRepository,
) : PlaylistInteractor {
    override suspend fun addPlayList(name: String, description: String?, uri: Uri?) {
        val id = UUID.randomUUID().toString()
        val playlistCoverUri = uri?.let {
            externalStorageRepository.savePlaylistCover(id, uri)
        }
        val playlist = Playlist(
            id = id,
            name = name,
            description = description,
            coverUri = playlistCoverUri
        )
        playlistRepository.addPlaylist(playlist)
    }

    override suspend fun updatePlaylist(
        playlist: Playlist,
        name: String,
        description: String?,
        uri: Uri?
    ) {
        playlist.apply {
            this.name = name
            description?.let { this.description = it }
            uri?.let { this.coverUri = externalStorageRepository.savePlaylistCover(this.id, uri) }
        }
        playlistRepository.addPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> = playlistRepository.getPlaylistsFlow()

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        playlistRepository.addTrackToPlaylist(playlist, track)
    }

    override fun getPlaylistById(playlistId: String): Flow<Playlist> {
        return playlistRepository.getPlaylist(playlistId).filterNotNull()
    }

    override fun getTracksByIds(tracksIds: List<String>): Flow<List<Track>> {
        return playlistRepository.getTracksByIds(tracksIds)
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: String, trackId: String) {
        playlistRepository.getPlaylist(playlistId)
            .filterNotNull()
            .collectLatest {
                val playlist = it.copy(
                    tracksIds = it.tracksIds.toMutableList().apply { remove(trackId) },
                    tracksCount = it.tracksIds.size
                )
                playlistRepository.addPlaylist(playlist)
                deleteNonPlaylistTrack(trackId)
            }
    }

    private suspend fun deleteNonPlaylistTrack(trackId: String) {
        playlistRepository.getPlaylistsFlow().collect {
            val trackInPlaylists = it.filter { track ->
                trackId in track.tracksIds.toSet()
            }
            if (trackInPlaylists.isEmpty()) {
                playlistRepository.deleteTrack(trackId)
            }
        }
    }

    override suspend fun deletePlaylist(playlistId: String) {
        playlistRepository.getPlaylist(playlistId)
            .collect { playlist ->
                playlist?.let {
                    playlistRepository.deletePlaylist(playlist)
                    playlist.tracksIds.forEach { deleteNonPlaylistTrack(it) }
                }
            }
    }
}