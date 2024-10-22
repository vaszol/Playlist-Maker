package com.practicum.playlistmaker.ui.playlist.create.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.NavigationInteractor
import com.practicum.playlistmaker.domain.PlaylistInteractor
import com.practicum.playlistmaker.domain.api.SharedPreferencesInteractor
import com.practicum.playlistmaker.ui.playlist.create.CreateEvent
import com.practicum.playlistmaker.ui.playlist.create.CreateScreenState
import com.practicum.playlistmaker.ui.search.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateViewModel(
    private val navigationInteractor: NavigationInteractor,
    private val sharedPreferencesInteractor: SharedPreferencesInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private var name = ""
    private var description: String? = null
    private val _playlistCoverUri: MutableLiveData<Uri?> = MutableLiveData()
    val playlistCoverUri: LiveData<Uri?> = _playlistCoverUri
    private val _isCreateBtnEnabled = MutableLiveData(false)
    val isCreateBtnEnabled: LiveData<Boolean> = _isCreateBtnEnabled
    private val _playlistId = MutableStateFlow<String?>(null)
    val playlistId: StateFlow<String?> = _playlistId

    private val _state = MutableLiveData<CreateScreenState>()
    val state: LiveData<CreateScreenState> = _state
    val event = SingleLiveEvent<CreateEvent>()

    init {
        subscribeOnPlaylist()
    }

    fun onPlaylistCoverSelected(uri: Uri) {
        _playlistCoverUri.value = uri
    }

    fun onNameChanged(newName: String) {
        name = newName
        _isCreateBtnEnabled.value = name.isNotEmpty()
    }

    fun onDescriptionChanged(newDescription: String) {
        description = newDescription
    }

    fun onCreateBtnClick() {
        if (name.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                state.value?.playlist?.let {
                    playlistInteractor.updatePlaylist(it, name, description, playlistCoverUri.value)
                } ?: run {
                    playlistInteractor.addPlayList(
                        name, description, playlistCoverUri.value
                    )
                }
                withContext(Dispatchers.Main) {
                    event.value = CreateEvent.SetPlaylistCreatedResult(name)
                    navigateBack()
                }
            }
        }
    }

    fun onBackPressed() {
        if (name.isNotEmpty() || !description.isNullOrEmpty() || playlistCoverUri.value != null) {
            event.value = CreateEvent.ShowBackConfirmationDialog
        } else {
            event.value = CreateEvent.NavigateBack
        }
    }

    fun onBackPressedConfirmed() = navigateBack()

    private fun navigateBack() {
        event.value = CreateEvent.NavigateBack
    }

    fun hideNavigation() {
        navigationInteractor.setBottomNavigationVisibility(false)
    }

    private fun subscribeOnPlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            _playlistId.update {
                sharedPreferencesInteractor.getPlaylistToEdit()
            }
            playlistId.collect { state ->
                state.let { it ->
                    playlistInteractor.getPlaylistById(it.toString())
                        .collectLatest {
                            _state.postValue(
                                getCurrentScreenState().copy(playlist = it)
                            )
                        }
                }
            }
        }
    }

    private fun getCurrentScreenState() = _state.value ?: CreateScreenState()
}