package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.dto.TrackRequest
import com.practicum.playlistmaker.data.dto.TrackResponse
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.models.Resource
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val trackDbConvertor: TrackDbConvertor,
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase
) : TrackRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackRequest(expression))

        if (response.resultCode == 200) {
            val tracks = (response as TrackResponse).results.map { trackDbConvertor.map(it) }
            val likedIds = appDatabase.trackDao().getTracksIds().toSet()
            emit(Resource.Success(tracks.map { it.copy(isLiked = it.trackId in likedIds) }))
        } else {
            emit(Resource.Error(response.resultCode.toString()))
        }
    }
}