package com.practicum.playlistmaker.ui.playlist.favorite

sealed class FavoriteScreenEvent {
    data object OpenPlayerScreen : FavoriteScreenEvent()
}