package com.practicum.playlistmaker.ui.playlist.playlistInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.practicum.playlistmaker.ui.playlist.playlistInfo.viewModel.PlaylistInfoViewModel
import com.practicum.playlistmaker.ui.search.TracksAdapter
import com.practicum.playlistmaker.ui.setSource
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistInfoFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistInfoBinding
    private val viewModel by viewModel<PlaylistInfoViewModel>()
    private lateinit var adapter: TracksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistInfoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.hideNavigation()
        adapter = TracksAdapter(viewModel::onTrackClick, viewModel::onLongClick)
        binding.apply {
            playlists.adapter = adapter
            infoToolbar.setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        }
        viewModel.state.observe(viewLifecycleOwner) { it ->
            it.playlist?.let {
                binding.apply {
                    it.coverUri?.let { img -> trackImg.setSource(img) }
                    name.text = it.name
                    description.text = it.description
                }
            }
            it.info?.let {
                binding.apply {
                    total.text = it
                }
            }
            adapter.updateList(it.tracks)
        }
        viewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                PlaylistInfoScreenEvent.NavigateBack -> findNavController().popBackStack()
                PlaylistInfoScreenEvent.OpenPlayerScreen -> findNavController().navigate(R.id.action_playlistInfoFragment_to_playerFragment)
                PlaylistInfoScreenEvent.ShowBackConfirmationDialog -> showConfirmDialog()
            }
        }
        setupBackPressHandling()
    }

    private fun showConfirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.playlist_info_dialog_title)
            .setMessage(R.string.playlist_info_dialog_message)
            .setNegativeButton(R.string.playlist_info_dialog_negative) { _, _ -> viewModel.onDeleteTrackDialogDismiss() }
            .setPositiveButton(R.string.playlist_info_dialog_positive) { _, _ -> viewModel.onDeleteTrackConfirmed() }
            .show()
    }

    private fun setupBackPressHandling() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isEnabled) {
                    viewModel.onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}