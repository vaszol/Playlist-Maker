package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.databinding.TrackItemBinding
import com.practicum.playlistmaker.domain.api.ImageInteractor
import com.practicum.playlistmaker.domain.api.ImageRepository

class ImageInteractorlmpl(private val repo: ImageRepository) : ImageInteractor {
    override fun fillBigImg(url: String, binding: ActivityMediaBinding) {
        repo.fillBigImg(url, binding)
    }

    override fun fillMiniImg(url: String, binding: TrackItemBinding) {
        repo.fillMiniImg(url, binding)
    }
}