package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.db.TrackDbRepository
import com.practicum.playlistmaker.domain.db.TracksDbInteractor
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class TracksDbInteractorImpl(
    private val trackDbRepository: TrackDbRepository
) : TracksDbInteractor {
    override suspend fun addToLiked(track: Track) {
        trackDbRepository.addTrackToLiked(track)
    }

    override suspend fun deleteFromLiked(track: Track) {
        trackDbRepository.deleteTrackFromLiked(track)
    }

    override suspend fun getLikedTracks(): Flow<List<Track>> {
        return trackDbRepository.getLikedTracks()
    }
}