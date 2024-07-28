package com.practicum.playlistmaker.ui.search

import com.practicum.playlistmaker.domain.models.Track

data class SearchScreenState(
    val searchHistoryVisible: Boolean = false,
    val messageVisible: Boolean = false,
    val messageFail: Boolean = false,
    val searchPgbVisible: Boolean = false,
    val tracks: List<Track> = listOf(),
    val trackSelected: Track? = null,
)