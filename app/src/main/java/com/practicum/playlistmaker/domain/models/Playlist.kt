package com.practicum.playlistmaker.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: String,
    var name: String,
    var description: String?,
    var coverUri: String?,
    val tracksIds: List<String> = listOf(),
    val tracksCount: Int = 0
) : Parcelable