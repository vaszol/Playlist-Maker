package com.practicum.playlistmaker.ui

import android.content.Context
import android.util.TypedValue
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
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

private const val IMAGE_SIZE = "512x512bb.jpg"
fun ImageView.load(imageUrl: String?, increaseQuality: Boolean = false) {
    val requestOptions = RequestOptions()
        .placeholder(R.drawable.ic_music_full)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
    val roundingCorners = resources.getDimensionPixelSize(R.dimen.corner_radius)
    val newImageUrl = if (increaseQuality) imageUrl?.replaceAfterLast('/', IMAGE_SIZE) else imageUrl
    Glide.with(context)
        .applyDefaultRequestOptions(requestOptions)
        .load(newImageUrl)
        .transform(CenterCrop(), RoundedCorners(roundingCorners))
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