package com.practicum.playlistmaker.itunes

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.MediaActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.SearchActivity

class SearchHistoryAdapter(private val context: Context) :
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
        holder.itemView.setOnClickListener {
            if ((context as SearchActivity).clickDebounce()) {
                val intent = Intent(context, MediaActivity::class.java)
                intent.putExtra("track", Gson().toJson(list[position]))
                context.startActivity(intent)
            }
        }
    }
}