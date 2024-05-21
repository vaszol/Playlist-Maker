package com.practicum.playlistmaker.itunes

import java.time.format.DateTimeFormatter

data class Track(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
) {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    fun getYear(): String? = DateTimeFormatter
        .ofPattern("yyyy").format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(releaseDate)
        )
}