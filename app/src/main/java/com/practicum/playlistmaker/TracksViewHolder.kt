package com.practicum.playlistmaker

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TracksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackImg: ImageView = itemView.findViewById(R.id.track_img)
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val trackArtist: TextView = itemView.findViewById(R.id.track_artist)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)

    fun bind(model: Track) {
        Glide.with(trackImg)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.ic_music)
            .fitCenter()
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2F, trackImg.context)))
            .into(trackImg)
        trackName.text = model.trackName
        trackArtist.text = model.artistName
        trackTime.text = model.trackTime
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

}