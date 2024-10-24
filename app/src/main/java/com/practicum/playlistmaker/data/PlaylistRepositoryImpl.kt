package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.domain.PlaylistRepository
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
    private val playlistDbConvertor: PlaylistDbConvertor,
) : PlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().addPlaylist(playlistDbConvertor.mapToEntity(playlist))
    }

    override fun getPlaylistsFlow(): Flow<List<Playlist>> =
        appDatabase.playlistDao().getPlaylistsFlow().map {
            it.map { entity -> playlistDbConvertor.map(entity) }
        }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        val tracksIds = playlist.tracksIds.toMutableList()
        tracksIds.add(track.trackId)
        val newPlaylist =
            playlist.copy(tracksIds = tracksIds, tracksCount = tracksIds.size)
        appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.mapToEntity(newPlaylist))
        appDatabase.playlistTrackDao().addTrack(trackDbConvertor.mapToPlaylistTrackEntity(track))
    }

    override fun getPlaylist(playlistId: String): Flow<Playlist?> {
        return appDatabase.playlistDao().getPlaylistFlow(playlistId).map { entity ->
            entity?.let { playlistDbConvertor.map(entity) }
        }
    }

    override fun getTracksByIds(tracksIds: List<String>): Flow<List<Track>> {
        return appDatabase.playlistTrackDao().getTracksByIds(tracksIds).map { entities ->
            entities.sortedByDescending { it.createdAt }
                .map { track -> trackDbConvertor.mapFromPlaylistTrackEntity(track) }
        }
    }

    override suspend fun deleteTrack(trackId: String) =
        appDatabase.playlistTrackDao().deleteTrack(trackId)

    override suspend fun deletePlaylist(playlist: Playlist) =
        appDatabase.playlistTrackDao().deletePlaylist(playlistDbConvertor.mapToEntity(playlist))
}