package com.practicum.playlistmaker.ui.playlist.playlistInfo

sealed class PlaylistInfoScreenEvent {
    object NavigateBack : PlaylistInfoScreenEvent()
}