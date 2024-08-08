package com.practicum.playlistmaker.data

import com.google.gson.Gson
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.SharedPreferencesConverter

class SharedPreferencesConverterImpl(private val gson: Gson) : SharedPreferencesConverter {

    override fun convertJsonToList(json: String): List<Track> =
        json.isEmpty().let { gson.fromJson(json, Array<Track>::class.java) }?.toList()
            ?: emptyList()

    override fun convertListToJson(tracks: List<Track>): String = gson.toJson(tracks)

    override fun convertTrackToJson(track: Track): String = gson.toJson(track)

    override fun convertJsonToTrack(json: String): Track = gson.fromJson(json, Track::class.java)
}