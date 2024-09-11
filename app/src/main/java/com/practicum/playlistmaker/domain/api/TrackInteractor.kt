package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>
}