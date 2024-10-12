package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.domain.db.TrackDbRepository
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackDbRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : TrackDbRepository {
    override suspend fun addTrackToLiked(track: Track) {
        appDatabase.trackDao()
            .addTrack(trackDbConvertor.mapToEntity(track, System.currentTimeMillis()))
    }

    override suspend fun deleteTrackFromLiked(track: Track) {
        appDatabase.trackDao().deleteTrack(trackDbConvertor.mapToEntity(track))
    }

    override suspend fun getLikedTracks(): Flow<List<Track>> {
        return appDatabase.trackDao().getTracks().map { it ->
            it.map { trackDbConvertor.map(it) }
        }
    }
}