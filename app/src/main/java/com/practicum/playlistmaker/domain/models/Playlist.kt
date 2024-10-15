package com.practicum.playlistmaker.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: String,
    val name: String,
    val description: String?,
    val coverUri: String?,
    val tracksIds: List<String> = listOf(),
    val tracksCount: Int = 0
) : Parcelable