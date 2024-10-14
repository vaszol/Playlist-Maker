package com.practicum.playlistmaker.ui.player

sealed class PlayerScreenEvent {
    object OpenBottomSheet : PlayerScreenEvent()
    object CloseBottomSheet : PlayerScreenEvent()
    data class ShowTrackAddedMessage(val playlistName: String) : PlayerScreenEvent()
    data class ShowTrackAlreadyInPlaylistMessage(val playlistName: String) : PlayerScreenEvent()
    object NavigateToCreatePlaylistScreen : PlayerScreenEvent()
}