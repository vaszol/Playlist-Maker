package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.domain.models.Playlist

class PlaylistDbConvertor {
    fun mapToEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            coverUri = playlist.coverUri,
            tracksIds = playlist.tracksIds.joinToString(),
            tracksCount = playlist.tracksCount
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            coverUri = playlist.coverUri,
            tracksIds = (playlist.tracksIds?.trim()?.splitToSequence(", ")
                ?.filter { it.isNotEmpty() }?.toList()
                ?: listOf()),
            tracksCount = playlist.tracksCount
        )
    }
}