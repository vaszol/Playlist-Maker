package com.practicum.playlistmaker.itunes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R

class TracksAdapter : RecyclerView.Adapter<TracksViewHolder>() {

    private var list: List<ItunesResult> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            (holder.itemView.context.applicationContext as App).addHistory(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<ItunesResult>) {
        this.list = list
        notifyDataSetChanged()
    }

}