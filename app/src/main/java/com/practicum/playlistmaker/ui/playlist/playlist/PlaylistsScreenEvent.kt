package com.practicum.playlistmaker.ui.playlist.playlist

sealed class PlaylistsScreenEvent {
    object NavigateToCreatePlaylist : PlaylistsScreenEvent()
    object NavigateToPlaylistInfo : PlaylistsScreenEvent()
}