package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.TrackRequest
import com.practicum.playlistmaker.data.dto.TrackResponse
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.models.Resource
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackRequest(expression))

        if (response.resultCode == 200) {
            emit(
                Resource.Success(
                    (response as TrackResponse).results.map {
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.collectionName ?: "",
                            it.releaseDate ?: "",
                            it.primaryGenreName ?: "",
                            it.country ?: "",
                            it.previewUrl ?: ""
                        )
                    })
            )
        } else {
            emit(Resource.Error(response.resultCode.toString()))
        }
    }
}