package com.practicum.playlistmaker.ui.search.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.api.SharedPreferencesInteractor
import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.search.SearchScreenEvent
import com.practicum.playlistmaker.ui.search.SearchScreenState
import com.practicum.playlistmaker.ui.search.SingleLiveEvent
import com.practicum.playlistmaker.ui.util.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val sharedPreferencesInteractor: SharedPreferencesInteractor,
) : ViewModel() {

    private val searchDebounce =
        debounce<Unit>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { _ ->
            search()
        }

    private val clickDebounce =
        debounce<Track>(CLICK_DEBOUNCE_DELAY, viewModelScope, false) { track ->
            viewModelScope.launch(Dispatchers.IO) {
                sharedPreferencesInteractor.addHistory(track)
                sharedPreferencesInteractor.setTrackToPlay(track)
            }
            _state.value = getCurrentScreenState().copy(trackSelected = track)
            event.value = SearchScreenEvent.OpenPlayerScreen
        }

    private var searchText: String = ""
    private val _state = MutableLiveData<SearchScreenState>()
    val state: LiveData<SearchScreenState> = _state
    val event = SingleLiveEvent<SearchScreenEvent>()

    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            sharedPreferencesInteractor.clearSharedPreference()
        }
        goneHistory()
    }

    fun doAfterTextChanged(text: String) {
        searchText = text
    }

    fun searchDebounce() = searchDebounce(Unit)

    fun focusChange(hasFocus: Boolean) {
        if (hasFocus && searchText.isEmpty()) showHistory() else goneHistory()
    }

    fun clearText() {
        event.value = SearchScreenEvent.HideKeyboard
        event.value = SearchScreenEvent.ClearSearch
    }

    fun search() {
        if (searchText.isNotEmpty()) {
            goneHistory()
            _state.postValue(SearchScreenState(searchPgbVisible = true, tracks = mutableListOf()))

            viewModelScope.launch(Dispatchers.IO) {
                trackInteractor.searchTracks(searchText)
                    .collect { pair ->
                        if (pair.second != null) messageFail()
                        else if (pair.first.isNullOrEmpty()) messageEmpty()
                        else messageOk(pair.first!!)
                    }
            }
        }
    }

    fun onTrackClick(track: Track) = clickDebounce(track)

    private fun goneHistory() {
        _state.postValue(
            SearchScreenState(
                searchHistoryVisible = false,
                tracks = emptyList(),
                messageVisible = false,
            )
        )
    }

    fun showTracks() {
        if (getCurrentScreenState().trackSelected == null == !getCurrentScreenState().searchHistoryVisible)
            showHistory()
    }

    fun showHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            sharedPreferencesInteractor.getFromHistory().let {
                _state.postValue(
                    SearchScreenState(
                        tracks = it,
                        searchHistoryVisible = true,
                        messageVisible = false,
                        searchPgbVisible = false,
                    )
                )
            }
        }
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

    private fun messageOk(tracks: List<Track>) {
        _state.postValue(
            SearchScreenState(
                tracks = tracks,
                messageVisible = false,
                searchPgbVisible = false,
            )
        )
    }

    private fun getCurrentScreenState() = state.value ?: SearchScreenState()

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 100L
    }
}