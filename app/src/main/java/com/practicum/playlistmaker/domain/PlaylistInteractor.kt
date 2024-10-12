package com.practicum.playlistmaker.domain

import android.net.Uri
import com.practicum.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun addPlayList(name: String, description: String?, value: Uri?)
    fun getPlaylists(): Flow<List<Playlist>>
}