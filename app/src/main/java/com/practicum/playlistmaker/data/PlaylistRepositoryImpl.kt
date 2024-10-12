package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.domain.PlaylistRepository
import com.practicum.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
) : PlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().addPlaylist(PlaylistEntity.mapFromDomain(playlist))
    }

    override fun getPlaylistsFlow(): Flow<List<Playlist>> =
        appDatabase.playlistDao().getPlaylistsFlow().map {
            it.map { entity -> entity.mapToDomain() }
        }
}