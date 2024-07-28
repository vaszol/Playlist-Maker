package com.practicum.playlistmaker.ui.search.viewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.api.SharedPreferencesInteractor
import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.domain.models.Resource
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.search.SearchScreenEvent
import com.practicum.playlistmaker.ui.search.SearchScreenState
import com.practicum.playlistmaker.ui.search.SingleLiveEvent

class SearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val sharedPreferencesInteractor: SharedPreferencesInteractor,
) : ViewModel() {

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val trackInteractor = Creator.provideTracksInteractor()
                val sharedPreferencesInteractor = Creator.provideSharedPreferencesInteractor()

                SearchViewModel(
                    trackInteractor,
                    sharedPreferencesInteractor
                )
            }
        }

        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var searchText: String = ""
    private val searchRunnable = Runnable { search() }
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private val _state = MutableLiveData<SearchScreenState>()
    val state: LiveData<SearchScreenState> = _state
    val event = SingleLiveEvent<SearchScreenEvent>()

    fun clearHistory() {
        sharedPreferencesInteractor.clearSharedPreference()
        goneHistory()
    }

    fun doAfterTextChanged(text: String) {
        searchText = text
    }

    fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun focusChange(hasFocus: Boolean) {
        if (hasFocus && searchText.isEmpty()) showHistory() else goneHistory()
    }

    fun clearText() {
        event.value = SearchScreenEvent.HideKeyboard
        event.value = SearchScreenEvent.ClearSearch
    }

    fun search() {
        handler.removeCallbacks(searchRunnable)
        goneHistory()
        _state.postValue(SearchScreenState(searchPgbVisible = true, tracks = mutableListOf()))
        trackInteractor.searchTracks(
            searchText,
            object : TrackInteractor.TracksConsumer {
                override fun consume(foundTracks: Resource<List<Track>>) = when (foundTracks) {
                    is Resource.Error -> messageFail()
                    is Resource.Success ->
                        if (foundTracks.data.isNullOrEmpty()) messageEmpty()
                        else messageOk(foundTracks)
                }
            }
        )
    }

    fun onTrackClick(track: Track) {
        if (clickDebounce()) {
            sharedPreferencesInteractor.addHistory(track)
            _state.value = getCurrentScreenState().copy(trackSelected = track)
            event.value = SearchScreenEvent.OpenPlayerScreen
        }
    }

    private fun goneHistory() {
        _state.postValue(
            SearchScreenState(
                searchHistoryVisible = false,
                tracks = emptyList(),
                messageVisible = false,
            )
        )
    }

    fun showHistory() {
        val tracks = sharedPreferencesInteractor.getFromHistory().toList().reversed()
        _state.postValue(
            SearchScreenState(
                tracks = tracks,
                searchHistoryVisible = true,
                messageVisible = false,
                searchPgbVisible = false,
            )
        )
    }

    private fun messageEmpty() {
        _state.postValue(
            SearchScreenState(
                searchHistoryVisible = false,
                tracks = emptyList(),
                messageVisible = true,
                messageFail = false,
                searchPgbVisible = false,
            )
        )
    }

    private fun messageFail() {
        _state.postValue(
            SearchScreenState(
                searchHistoryVisible = false,
                tracks = emptyList(),
                messageVisible = true,
                messageFail = true,
                searchPgbVisible = false,
            )
        )
    }

    private fun messageOk(response: Resource<List<Track>>) {
        val tracks = response.data ?: listOf()
        _state.postValue(
            SearchScreenState(
                tracks = tracks,
                messageVisible = false,
                searchPgbVisible = false,
            )
        )
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed(
                { isClickAllowed = true },
                CLICK_DEBOUNCE_DELAY
            )
        }
        return current
    }

    private fun getCurrentScreenState() = state.value ?: SearchScreenState()
}