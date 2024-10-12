package com.practicum.playlistmaker.domain.db

import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackDbRepository {
    suspend fun addTrackToLiked(track: Track)
    suspend fun deleteTrackFromLiked(track: Track)
    suspend fun getLikedTracks(): Flow<List<Track>>
}