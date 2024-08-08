package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Resource
import com.practicum.playlistmaker.domain.models.Track

interface TrackInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    fun interface TracksConsumer {
        fun consume(foundTracks: Resource<List<Track>>)
    }
}