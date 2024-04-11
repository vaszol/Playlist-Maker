package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TracksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackImg: ImageView = itemView.findViewById(R.id.track_img)
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val trackArtist: TextView = itemView.findViewById(R.id.track_artist)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)

    fun bind(model: Track) {
        Glide.with(trackImg)
            .load(model.artworkUrl100)
            .fitCenter()
            .centerCrop()
            .into(trackImg)
        trackName.text = model.trackName
        trackArtist.text = model.artistName
        trackTime.text = model.trackTime
    }

}