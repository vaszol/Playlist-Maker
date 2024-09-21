package com.practicum.playlistmaker.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.dto.TrackRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItunesApiClient(private val itunesApi: ItunesApi, private val context: Context) :
    NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        return withContext(Dispatchers.IO) {
            try {
                if (!isConnected()) {
                    Response().apply { resultCode = -1 }
                }
                if (dto is TrackRequest) {
                    itunesApi.search(dto.expression).apply {
                        resultCode = 200
                    }
                } else {
                    Response().apply { resultCode = 400 }
                }
            } catch (exception: Exception) {
                Response().apply { resultCode = 400 }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}