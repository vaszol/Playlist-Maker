package com.practicum.playlistmaker.itunes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.SearchActivity

class SearchHistoryAdapter(searchActivity: SearchActivity) :
    RecyclerView.Adapter<TracksViewHolder>() {
    private val context: Any = searchActivity.applicationContext
    private var list: List<ItunesResult> = (context as App).getFromHistory().reversed()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TracksViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(list[position])
    }
}