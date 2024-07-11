package com.practicum.playlistmaker.data

import android.content.Context
import android.util.TypedValue
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.databinding.TrackItemBinding
import com.practicum.playlistmaker.domain.api.ImageRepository

class ImageRepositoryImpl : ImageRepository {
    override fun fillBigImg(url: String, binding: ActivityMediaBinding) {
        Glide.with(binding.trackImg)
            .load(getCoverArtwork(url))
            .placeholder(R.drawable.ic_music_full)
            .fitCenter()
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2F, binding.trackImg.context)))
            .into(binding.trackImg)
    }

    override fun fillMiniImg(url: String, binding: TrackItemBinding) {
        Glide.with(binding.trackImg)
            .load(url)
            .placeholder(R.drawable.ic_music_full)
            .fitCenter()
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2F, binding.trackImg.context)))
            .into(binding.trackImg)
    }

    private fun getCoverArtwork(url: String): String {
        return url.replaceAfterLast('/', "512x512bb.jpg")
    }

    @Suppress("SameParameterValue")
    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}