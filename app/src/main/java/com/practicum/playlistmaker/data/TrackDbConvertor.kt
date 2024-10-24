package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.db.entity.LikedTrackEntity
import com.practicum.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.domain.models.Track

class TrackDbConvertor {
    fun mapToLikedTrackEntity(
        track: Track,
        createDate: Long = 0L
    ): LikedTrackEntity {
        return LikedTrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            createDate,
        )
    }

    fun mapFromLikedTrackEntity(track: LikedTrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            true
        )
    }

    fun mapToPlaylistTrackEntity(
        track: Track,
        createDate: Long = 0L
    ): PlaylistTrackEntity {
        return PlaylistTrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            createDate,
        )
    }

    fun mapFromPlaylistTrackEntity(track: PlaylistTrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            true
        )
    }

    fun map(track: TrackDto): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName ?: "",
            track.releaseDate ?: "",
            track.primaryGenreName ?: "",
            track.country ?: "",
            track.previewUrl ?: "",
        )
    }
}