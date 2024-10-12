package com.practicum.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.practicum.playlistmaker.domain.models.Playlist

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String?,
    val coverUri: String?,
    val tracksIds: String?,
    val tracksCount: Int = 0
) {

    companion object {
        fun mapFromDomain(playlist: Playlist) = PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            coverUri = playlist.coverUri,
            tracksIds = playlist.tracksIds.joinToString(),
            tracksCount = playlist.tracksCount
        )
    }

    fun mapToDomain(): Playlist = Playlist(
        id = id,
        name = name,
        description = description,
        coverUri = coverUri,
        tracksIds = (tracksIds?.trim()?.splitToSequence(", ")?.filter { it.isNotEmpty() }?.toList()
            ?: listOf()),
        tracksCount = tracksCount
    )
}