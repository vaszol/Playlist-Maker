package com.practicum.playlistmaker.domain.db

import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksDbInteractor {
    suspend fun addToLiked(track: Track)
    suspend fun deleteFromLiked(track: Track)
    suspend fun getLikedTracks(): Flow<List<Track>>
}