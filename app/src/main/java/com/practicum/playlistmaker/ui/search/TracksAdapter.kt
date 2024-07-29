package com.practicum.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.TrackItemBinding
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TracksAdapter(
    private var model: MutableList<Track>,
    private val clickListener: ClickListener,
) : RecyclerView.Adapter<TracksAdapter.ViewHolder>() {

    private lateinit var tracksBinding: TrackItemBinding
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
            Glide.with(binding.trackImg)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.ic_music)
                .fitCenter()
                .centerCrop()
                .transform(RoundedCorners(dpToPx(2F, binding.trackImg.context)))
                .into(binding.trackImg)
            binding.trackName.text = model.trackName
            binding.trackArtist.text = model.artistName
            binding.trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
        }
    }

    fun interface ClickListener {
        fun onClick(track: Track)
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}