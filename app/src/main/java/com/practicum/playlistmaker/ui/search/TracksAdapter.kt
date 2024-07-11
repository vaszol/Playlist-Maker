package com.practicum.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.TrackItemBinding
import com.practicum.playlistmaker.domain.Creator
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TracksAdapter(
    private var model: MutableList<Track>,
    private val clickListener: ClickListener,
) : RecyclerView.Adapter<TracksAdapter.ViewHolder>() {

    private lateinit var tracksBinding: TrackItemBinding
    private var creatorImage = Creator.provideImageInteractor()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        tracksBinding = TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(tracksBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(model[position])
        holder.itemView.setOnClickListener { clickListener.onClick(model[position]) }
    }

    override fun getItemCount(): Int {
        return model.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(model: List<Track>?) {
        this.model = model as MutableList<Track>
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: TrackItemBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        fun bind(model: Track) {
            creatorImage.fillMiniImg(model.artworkUrl100, binding)
            binding.trackName.text = model.trackName
            binding.trackArtist.text = model.artistName
            binding.trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
        }
    }

    fun interface ClickListener {
        fun onClick(track: Track)
    }
}