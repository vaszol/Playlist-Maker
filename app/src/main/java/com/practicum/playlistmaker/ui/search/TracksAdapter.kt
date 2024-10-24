package com.practicum.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.TrackItemBinding
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.setSource
import java.text.SimpleDateFormat
import java.util.Locale

class TracksAdapter(
    private val clickListener: (track: Track) -> Unit,
    private val longClickListener: ((trackId: String) -> Unit)? = null,
) : RecyclerView.Adapter<TracksAdapter.ViewHolder>() {
    private var model = emptyList<Track>()
    private lateinit var tracksBinding: TrackItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        tracksBinding = TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(tracksBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(model[position])
        holder.itemView.apply {
            setOnClickListener { clickListener(model[position]) }
            setOnLongClickListener {
                longClickListener?.let { onLongClick ->
                    onLongClick(model[position].trackId)
                }
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return model.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(model: List<Track>) {
        this.model = model
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: TrackItemBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        fun bind(model: Track) {
            binding.apply {
                trackImg.setSource(model.artworkUrl100)
                trackName.text = model.trackName
                trackArtist.text = model.artistName
                trackTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
            }
        }
    }
}