package com.practicum.playlistmaker.ui.playlist.create.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.NavigationInteractor
import com.practicum.playlistmaker.domain.PlaylistInteractor
import com.practicum.playlistmaker.ui.playlist.create.CreateEvent
import com.practicum.playlistmaker.ui.search.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateViewModel(
    private val navigationInteractor: NavigationInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    protected var name = ""
    protected var description: String? = null
    protected val _playlistCoverUri: MutableLiveData<Uri?> = MutableLiveData()
    val playlistCoverUri: LiveData<Uri?> = _playlistCoverUri
    private val _isCreateBtnEnabled = MutableLiveData<Boolean>(false)
    val isCreateBtnEnabled: LiveData<Boolean> = _isCreateBtnEnabled
    val event = SingleLiveEvent<CreateEvent>()

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
                playlistInteractor.addPlayList(
                    name, description, playlistCoverUri.value
                )
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
            navigationInteractor.setBottomNavigationVisibility(true)
        }
    }

    fun onBackPressedConfirmed() = navigateBack()

    private fun navigateBack() {
        event.value = CreateEvent.NavigateBack
        navigationInteractor.setBottomNavigationVisibility(true)
    }
}