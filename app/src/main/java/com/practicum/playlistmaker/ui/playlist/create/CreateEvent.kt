package com.practicum.playlistmaker.ui.playlist.create

sealed class CreateEvent {
    object NavigateBack : CreateEvent()
    object ShowBackConfirmationDialog : CreateEvent()
    data class SetPlaylistCreatedResult(val playlistName: String) : CreateEvent()
}