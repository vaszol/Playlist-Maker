package com.practicum.playlistmaker.itunes

data class ItunesResult(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
)