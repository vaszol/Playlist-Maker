package com.practicum.playlistmaker.domain

import android.net.Uri

interface ExternalStorageRepository {
    suspend fun savePlaylistCover(playlistId: String, uri: Uri): String
}