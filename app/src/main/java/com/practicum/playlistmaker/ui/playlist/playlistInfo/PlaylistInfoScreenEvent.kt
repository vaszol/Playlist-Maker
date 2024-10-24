package com.practicum.playlistmaker.ui.playlist.playlistInfo

sealed class PlaylistInfoScreenEvent {
    object NavigateBack : PlaylistInfoScreenEvent()
    data object OpenPlayerScreen : PlaylistInfoScreenEvent()
    object ShowDeleteTrackConfirmationDialog : PlaylistInfoScreenEvent()
    object ShowEmptyPlaylistDialog : PlaylistInfoScreenEvent()
    data class ShowMenu(val isVisibleMenu: Boolean) : PlaylistInfoScreenEvent()
    object SharePlaylist : PlaylistInfoScreenEvent()
    object ShowDeletePlaylistConfirmationDialog : PlaylistInfoScreenEvent()
    data object OpenEditPlaylistScreen : PlaylistInfoScreenEvent()
}