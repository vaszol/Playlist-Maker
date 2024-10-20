package com.practicum.playlistmaker.ui.playlist.playlist

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.ui.playlist.playlist.viewModel.PlaylistViewModel
import com.practicum.playlistmaker.ui.util.ResultKeyHolder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel by viewModel<PlaylistViewModel>()
    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater)
        binding.apply {
            messageBtn.setOnClickListener { viewModel.onCreateBtnClick() }
            messageText.text = getString(R.string.playlist_empty)
            messageImg.setImageResource(
                if (Configuration.UI_MODE_NIGHT_YES == resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) R.drawable.ic_message_empty_dark
                else R.drawable.ic_message_empty
            )
        }
        playlistAdapter = PlaylistAdapter(viewModel::onPlaylistClicked)
        binding.myPlaylists.adapter = playlistAdapter
        requireActivity().supportFragmentManager.setFragmentResultListener(
            ResultKeyHolder.KEY_PLAYLIST_CREATED,
            viewLifecycleOwner
        ) { _, bundle ->
            bundle.getString(ResultKeyHolder.KEY_PLAYLIST_NAME)
                ?.let { showPlaylistCreatedMessage(it) }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.showNavigation()
        viewModel.playlist.observe(viewLifecycleOwner) {
            playlistAdapter.submitList(it)
            binding.emptyGroup.isVisible = it.isEmpty()
            binding.messageImg.isVisible = it.isEmpty()
            binding.messageText.isVisible = it.isEmpty()
        }
        viewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                PlaylistsScreenEvent.NavigateToCreatePlaylist -> findNavController().navigate(R.id.action_mediaFragment_to_createFragment)
                PlaylistsScreenEvent.NavigateToPlaylistInfo -> findNavController().navigate(R.id.action_mediaFragment_to_playlistInfoFragment)
            }
        }
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }

    private fun showPlaylistCreatedMessage(playlistName: String) {
        Toast.makeText(
            requireContext(),
            getString(R.string.playlist_created_snackbar, playlistName),
            Toast.LENGTH_SHORT
        )
            .show()
    }
}