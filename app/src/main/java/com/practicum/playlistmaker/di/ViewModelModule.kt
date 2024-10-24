package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.ui.main.MainViewModel
import com.practicum.playlistmaker.ui.playlist.favorite.viewModel.FavoriteViewModel
import com.practicum.playlistmaker.ui.player.viewModel.PlyaerViewModel
import com.practicum.playlistmaker.ui.playlist.create.viewModel.CreateViewModel
import com.practicum.playlistmaker.ui.playlist.playlist.viewModel.PlaylistViewModel
import com.practicum.playlistmaker.ui.playlist.playlistInfo.viewModel.PlaylistInfoViewModel
import com.practicum.playlistmaker.ui.search.viewModel.SearchViewModel
import com.practicum.playlistmaker.ui.settings.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {

    viewModel<PlyaerViewModel> {
        PlyaerViewModel(get(), get(), get(), get(), get())
    }

    viewModel<SearchViewModel> {
        SearchViewModel(get(), get(), get())
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(get())
    }

    viewModel<FavoriteViewModel> {
        FavoriteViewModel(get(), get(), get())
    }

    viewModel<PlaylistViewModel> {
        PlaylistViewModel(get(), get(), get())
    }

    viewModel<CreateViewModel> {
        CreateViewModel(get(), get(), get())
    }

    viewModel<MainViewModel> {
        MainViewModel(get())
    }

    viewModel<PlaylistInfoViewModel> {
        PlaylistInfoViewModel(get(), get(), get())
    }
}