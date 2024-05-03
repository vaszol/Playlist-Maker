package com.practicum.playlistmaker.itunes

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ItunesApiService {
    val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val build: ItunesApi = retrofit.create(ItunesApi::class.java)
}