package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.ui.main.MainViewModel
import com.practicum.playlistmaker.ui.playlist.favorite.viewModel.FavoriteViewModel
import com.practicum.playlistmaker.ui.media.viewModel.MediaViewModel
import com.practicum.playlistmaker.ui.playlist.create.viewModel.CreateViewModel
import com.practicum.playlistmaker.ui.playlist.playlist.viewModel.PlaylistViewModel
import com.practicum.playlistmaker.ui.search.viewModel.SearchViewModel
import com.practicum.playlistmaker.ui.settings.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {

    viewModel<MediaViewModel> {
        MediaViewModel(get(), get(), get())
    }

    viewModel<SearchViewModel> {
        SearchViewModel(get(), get())
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(get())
    }

    viewModel<FavoriteViewModel> {
        FavoriteViewModel(get(), get())
    }

    viewModel<PlaylistViewModel> {
        PlaylistViewModel(get())
    }

    viewModel<CreateViewModel> {
        CreateViewModel(get(), get())
    }

    viewModel<MainViewModel> {
        MainViewModel(get())
    }
}