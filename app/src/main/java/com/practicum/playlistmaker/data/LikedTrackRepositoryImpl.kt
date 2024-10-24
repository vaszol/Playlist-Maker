package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.domain.db.TrackDbRepository
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LikedTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : TrackDbRepository {
    override suspend fun addTrackToLiked(track: Track) {
        appDatabase.likedTrackDao()
            .addTrack(trackDbConvertor.mapToLikedTrackEntity(track, System.currentTimeMillis()))
    }

    override suspend fun deleteTrackFromLiked(track: Track) {
        appDatabase.likedTrackDao().deleteTrack(trackDbConvertor.mapToLikedTrackEntity(track))
    }

    override suspend fun getLikedTracks(): Flow<List<Track>> {
        return appDatabase.likedTrackDao().getTracks().map { it ->
            it.map { trackDbConvertor.mapFromLikedTrackEntity(it) }
        }
    }
}