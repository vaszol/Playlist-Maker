package com.practicum.playlistmaker.itunes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R

class ItunesAdapter() : RecyclerView.Adapter<ItunesViewHolder>() {

    private var list: List<ItunesResult> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItunesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return ItunesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItunesViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(list: List<ItunesResult>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun clear() {
        list = emptyList()
        notifyDataSetChanged()
    }

}