package com.practicum.playlistmaker.domain.api

import android.content.Context

interface IntentActionInteractor {
    fun send(context: Context)
    fun sendTo(context: Context)
    fun view(context: Context)
}