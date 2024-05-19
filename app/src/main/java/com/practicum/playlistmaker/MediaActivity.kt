package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
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

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

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
        if (trackImg != null) {
            fillImg(trackImg!!, track.getCoverArtwork())
        }
        trackName?.text = track.trackName
        trackArtist?.text = track.artistName
        infoTrackTime?.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        if (track.collectionName.isNullOrEmpty()) infoTrackCollectionGroup?.visibility = View.GONE
        track.collectionName.also { infoTrackCollection?.text = it }
        infoTrackReleaseDate?.text = track.releaseDate
        infoTrackGenre?.text = track.primaryGenreName
        infoTrackCountry?.text = track.country
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