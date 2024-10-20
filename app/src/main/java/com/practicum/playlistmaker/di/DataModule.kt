package com.practicum.playlistmaker.di

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.SharedPreferencesConverterImpl
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.network.ItunesApi
import com.practicum.playlistmaker.data.network.ItunesApiClient
import com.practicum.playlistmaker.domain.SharedPreferencesConverter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val itunesBaseUrl = "https://itunes.apple.com"
private const val KEY_SHARED_PREFERENCES = "shared_preferences"

val DataModule = module {

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(itunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<ItunesApi> {
        get<Retrofit>().create(ItunesApi::class.java)
    }

    single<NetworkClient> {
        ItunesApiClient(get(), get())
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(KEY_SHARED_PREFERENCES, Application.MODE_PRIVATE)
    }

    single<SharedPreferencesConverter> {
        SharedPreferencesConverterImpl(gson = get())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "PlaylistMaker.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    factory<Gson> { Gson() }
}