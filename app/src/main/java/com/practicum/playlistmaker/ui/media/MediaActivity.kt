package com.practicum.playlistmaker.ui.media

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.domain.models.PlayerStateEnum
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.media.viewModel.MediaViewModel
import com.practicum.playlistmaker.ui.setSource
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaActivity : AppCompatActivity() {
    private lateinit var mediaBinding: ActivityMediaBinding
    private val viewModel by viewModel<MediaViewModel>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaBinding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(mediaBinding.root)
        viewModel.setTrack()
        viewModel.state.observe(this) {
            it.track?.let(::setTrackData)
            mediaBinding.trackTime.text = it.trackTime.ifEmpty { "00:00" }
            it.playerState.let { playerStateEnum: PlayerStateEnum ->
                when (playerStateEnum) {
                    PlayerStateEnum.STATE_PLAYING -> mediaBinding.playBtn.setImageResource(R.drawable.ic_pause)
                    PlayerStateEnum.STATE_PAUSED -> mediaBinding.playBtn.setImageResource(R.drawable.ic_play)
                }
            }
            if (it.isLiked)
                mediaBinding.likeBtn.setImageResource(R.drawable.ic_like)
            else
                mediaBinding.likeBtn.setImageResource(R.drawable.ic_not_like)
        }

        mediaBinding.apply {
            mediaToolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            playBtn.setOnClickListener { viewModel.play() }
            likeBtn.setOnClickListener { viewModel.like() }
        }
    }

    private fun setTrackData(track: Track) {
        mediaBinding.apply {
            trackImg.setSource(track.getCoverArtwork())
            trackName.text = track.trackName
            trackArtist.text = track.artistName
            infoTrackCollectionGroup.isVisible = track.collectionName.isNotEmpty()
            track.collectionName.also { infoTrackCollectionVal.text = it }
            infoTrackReleaseDateVal.text = track.getYear()
            infoTrackGenreVal.text = track.primaryGenreName
            infoTrackCountryVal.text = track.country

        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stop()
    }
}