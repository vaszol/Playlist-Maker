package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Resource
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}