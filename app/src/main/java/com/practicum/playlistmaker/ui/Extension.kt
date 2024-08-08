package com.practicum.playlistmaker.ui

import android.content.Context
import android.util.TypedValue
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R

fun ImageView.setSource(url: String) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.ic_music_full)
        .fitCenter()
        .centerCrop()
        .transform(RoundedCorners(dpToPx(2F, context)))
        .into(this)
}

@Suppress("SameParameterValue")
private fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}