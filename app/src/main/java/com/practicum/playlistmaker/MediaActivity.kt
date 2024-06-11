package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.itunes.Track
import java.text.SimpleDateFormat
import java.util.Locale

class MediaActivity : AppCompatActivity() {

    private var playBtn: ImageView? = null
    private var trackTime: TextView? = null
    private var toolbar: Toolbar? = null
    private var trackImg: ImageView? = null
    private var trackName: TextView? = null
    private var trackArtist: TextView? = null
    private var infoTrackTime: TextView? = null
    private var infoTrackCollectionGroup: Group? = null
    private var infoTrackCollection: TextView? = null
    private var infoTrackReleaseDate: TextView? = null
    private var infoTrackGenre: TextView? = null
    private var infoTrackCountry: TextView? = null
    private var mediaPlayer = MediaPlayer()
    private var mainThreadHandler: Handler? = null
    private var mediaRunnable: Runnable? = null

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 1000L
    }

    private var playerState = STATE_DEFAULT
    private var seconds = 0L
    private var startTime = 0L
    private var duration = 0L

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        mainThreadHandler = Handler(Looper.getMainLooper())

        playBtn = findViewById(R.id.play_btn)
        trackTime = findViewById(R.id.track_time)
        toolbar = findViewById(R.id.media_toolbar)
        trackImg = findViewById(R.id.track_img)
        trackName = findViewById(R.id.track_name)
        trackArtist = findViewById(R.id.track_artist)
        infoTrackTime = findViewById(R.id.info_track_time_val)
        infoTrackCollectionGroup = findViewById(R.id.info_track_collection_group)
        infoTrackCollection = findViewById(R.id.info_track_collection_val)
        infoTrackReleaseDate = findViewById(R.id.info_track_release_date_val)
        infoTrackGenre = findViewById(R.id.info_track_Genre_val)
        infoTrackCountry = findViewById(R.id.info_track_country_val)

        val track = Gson().fromJson(intent.getStringExtra("track"), Track::class.java)
        toolbar?.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        playBtn?.setOnClickListener { playbackControl() }
        if (track != null) {
            if (trackImg != null) {
                fillImg(trackImg!!, track.getCoverArtwork())
            }
            trackName?.text = track.trackName
            trackArtist?.text = track.artistName
            infoTrackTime?.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            if (track.collectionName.isNullOrEmpty()) infoTrackCollectionGroup?.visibility =
                View.GONE
            track.collectionName.also { infoTrackCollection?.text = it }
            infoTrackReleaseDate?.text = track.getYear()
            infoTrackGenre?.text = track.primaryGenreName
            infoTrackCountry?.text = track.country
            preparePlayer(track.previewUrl)
        }
        mediaRunnable = object : Runnable {
            @SuppressLint("DefaultLocale")
            override fun run() {
                val elapsedTime = System.currentTimeMillis() - startTime
                val remainingTime = duration + elapsedTime

                if (remainingTime < 30000) {
                    seconds = remainingTime / DELAY
                    trackTime?.text = String.format("%02d:%02d", seconds / 60, seconds % 60)
                    mainThreadHandler?.postDelayed(this, DELAY)
                } else {
                    trackTime?.text = String.format("%02d:%02d", 0, 0)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playBtn?.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playBtn?.setImageResource(R.drawable.ic_play)
            playerState = STATE_PREPARED
            seconds = 0
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playBtn?.setImageResource(R.drawable.ic_pause)
        playerState = STATE_PLAYING
        startTime = System.currentTimeMillis()
        duration = seconds * DELAY
        mainThreadHandler?.post(mediaRunnable!!)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playBtn?.setImageResource(R.drawable.ic_play)
        playerState = STATE_PAUSED
        mainThreadHandler?.removeCallbacks(mediaRunnable!!)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun fillImg(trackImg: ImageView, url: String) {
        Glide.with(trackImg)
            .load(url)
            .placeholder(R.drawable.ic_music_full)
            .fitCenter()
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2F, trackImg.context)))
            .into(trackImg)
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}