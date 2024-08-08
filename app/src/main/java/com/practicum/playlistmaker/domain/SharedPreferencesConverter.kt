package com.practicum.playlistmaker.domain

import com.practicum.playlistmaker.domain.models.Track

interface SharedPreferencesConverter {
    fun convertJsonToList(json: String): List<Track>
    fun convertListToJson(tracks: List<Track>): String
    fun convertTrackToJson(track: Track): String
    fun convertJsonToTrack(json: String): Track
}