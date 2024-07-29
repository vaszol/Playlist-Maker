package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.TrackRequest
import com.practicum.playlistmaker.data.dto.TrackResponse
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.models.Resource
import com.practicum.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackRequest(expression))

        return if (response.resultCode == 200) {
            Resource.Success(
                (response as TrackResponse).results.map {
                    Track(
                        it.trackId, it.trackName, it.artistName, it.trackTimeMillis,
                        it.artworkUrl100, it.collectionName, it.releaseDate, it.primaryGenreName,
                        it.country, it.previewUrl
                    )
                })
        } else {
            Resource.Error(response.resultCode.toString())
        }
    }
}