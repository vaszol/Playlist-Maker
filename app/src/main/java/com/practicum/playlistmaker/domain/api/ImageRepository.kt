package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.databinding.TrackItemBinding

interface ImageRepository {
    fun fillBigImg(url: String, binding: ActivityMediaBinding)
    fun fillMiniImg(url: String, binding: TrackItemBinding)
}