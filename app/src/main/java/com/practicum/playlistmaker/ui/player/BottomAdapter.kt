package com.practicum.playlistmaker.ui.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemBottomSheetBinding
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.ui.load

class BottomAdapter(
    private val onPlaylistClicked: (Playlist) -> Unit
) : ListAdapter<Playlist, BottomAdapter.PlaylistViewHolder>(PlaylistDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(ItemBottomSheetBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlaylistViewHolder(
        private val binding: ItemBottomSheetBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(playlist: Playlist) {
            with(binding) {
                root.setOnClickListener { onPlaylistClicked(playlist) }
                playlist.coverUri?.let { cover.load(it) }
                name.text = playlist.name
                count.text = getTracksCountText(playlist.tracksCount)
            }
        }

        private fun getTracksCountText(tracksCount: Int): String {
            val pluralsString = binding.root.resources.getQuantityString(
                R.plurals.track,
                tracksCount,
                tracksCount
            )
            return "$tracksCount $pluralsString"
        }
    }

    private class PlaylistDiffCallback : DiffUtil.ItemCallback<Playlist>() {

        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist) =
            oldItem == newItem
    }
}