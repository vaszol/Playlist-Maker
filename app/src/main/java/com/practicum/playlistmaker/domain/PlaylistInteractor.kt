package com.practicum.playlistmaker.domain

import android.net.Uri

interface PlaylistInteractor {
    suspend fun addPlayList(name: String, description: String?, value: Uri?)
}