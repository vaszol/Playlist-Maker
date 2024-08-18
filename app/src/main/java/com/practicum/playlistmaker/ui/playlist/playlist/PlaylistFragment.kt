package com.practicum.playlistmaker.ui.playlist.playlist

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.ui.playlist.playlist.viewModel.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel by viewModel<PlaylistViewModel>()

    companion object {
        fun newInstance() = PlaylistFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater)
        binding.apply {
            messageText.text = getString(R.string.playlist_empty)
            messageImg.setImageResource(
                if (Configuration.UI_MODE_NIGHT_YES == resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) R.drawable.ic_message_empty_dark
                else R.drawable.ic_message_empty
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.playlist.observe(viewLifecycleOwner) {
            binding.messageImg.isVisible = it.isEmpty()
            binding.messageText.isVisible = it.isEmpty()
        }
    }
}