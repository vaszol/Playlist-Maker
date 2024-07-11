package com.practicum.playlistmaker.ui.media

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.domain.Creator
import com.practicum.playlistmaker.domain.models.PlayerStateEnum
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class MediaActivity : AppCompatActivity() {
    lateinit var mediaBinding: ActivityMediaBinding
    private var mediaPlayer = MediaPlayer()
    private var mainThreadHandler: Handler? = null
    private var mediaRunnable: Runnable? = null
    private var creatorPlayer = Creator.providePlayerInteractor()
    private var creatorImage = Creator.provideImageInteractor()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaBinding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(mediaBinding.root)

        mainThreadHandler = Handler(Looper.getMainLooper())
        val track = Gson().fromJson(intent.getStringExtra("track"), Track::class.java)
        mediaBinding.mediaToolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        if (track != null) {
            creatorImage.fillBigImg(track.artworkUrl100, mediaBinding)
            mediaBinding.trackName.text = track.trackName
            mediaBinding.trackArtist.text = track.artistName
            mediaBinding.infoTrackTimeVal.text =
                SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(track.trackTimeMillis)
            if (track.collectionName.isNullOrEmpty()) mediaBinding.infoTrackCollectionGroup.visibility =
                View.GONE
            track.collectionName.also { mediaBinding.infoTrackCollectionVal.text = it }
            mediaBinding.infoTrackReleaseDateVal.text = track.getYear()
            mediaBinding.infoTrackGenreVal.text = track.primaryGenreName
            mediaBinding.infoTrackCountryVal.text = track.country
            preparePlayer(track.previewUrl)
        }
        mediaRunnable = object : Runnable {
            @SuppressLint("DefaultLocale")
            override fun run() {
                if (mediaPlayer.isPlaying) {
                    mediaBinding.trackTime.text =
                        SimpleDateFormat("mm:ss", Locale.getDefault())
                            .format(mediaPlayer.currentPosition)
                    mainThreadHandler?.postDelayed(this, 1000L)
                } else {
                    creatorPlayer.pausePlayer(mediaPlayer, mediaBinding)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mediaBinding.playBtn.setOnClickListener {
            creatorPlayer.playbackControl(mediaPlayer, mediaBinding)
            mainThreadHandler?.post(mediaRunnable!!)
        }
    }

    override fun onPause() {
        super.onPause()
        creatorPlayer.pausePlayer(mediaPlayer, mediaBinding)
        mainThreadHandler?.removeCallbacks(mediaRunnable!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaBinding.playBtn.isEnabled = true
            creatorPlayer.playerState = PlayerStateEnum.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            mediaBinding.playBtn.setImageResource(R.drawable.ic_play)
            creatorPlayer.playerState = PlayerStateEnum.STATE_PREPARED
        }
    }
}