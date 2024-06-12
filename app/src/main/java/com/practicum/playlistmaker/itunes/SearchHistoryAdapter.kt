package com.practicum.playlistmaker.itunes

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R

class SearchHistoryAdapter(context: Context, private val clickListener: ClickListener) :
    RecyclerView.Adapter<TracksViewHolder>() {
    private var list: List<Track> = (context.applicationContext as App).getFromHistory().reversed()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TracksViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener { clickListener.onClick(list[position]) }
    }

    fun interface ClickListener {
        fun onClick(track: Track)
    }
}