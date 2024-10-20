package com.practicum.playlistmaker.ui.playlist.playlistInfo

sealed class PlaylistInfoScreenEvent {
    object NavigateBack : PlaylistInfoScreenEvent()
    data object OpenPlayerScreen : PlaylistInfoScreenEvent()
    object ShowBackConfirmationDialog : PlaylistInfoScreenEvent()
}