package com.practicum.playlistmaker.domain.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.IntentActionInteractor

class IntentActionInteractorlmpl : IntentActionInteractor {
    override fun send(context: Context) {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.android_developer))
            context.startActivity(this)
        }
    }

    override fun sendTo(context: Context) {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(context.getString(R.string.mailto))
            putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.EXTRA_EMAIL)))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.EXTRA_SUBJECT))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.EXTRA_TEXT))
            context.startActivity(this)
        }
    }

    override fun view(context: Context) {
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(context.getString(R.string.practicum_offer))
            context.startActivity(this)
        }
    }
}