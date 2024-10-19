package com.practicum.playlistmaker.ui.playlist.playlistInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.practicum.playlistmaker.ui.playlist.playlistInfo.viewModel.PlaylistInfoViewModel
import com.practicum.playlistmaker.ui.setSource
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistInfoFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistInfoBinding
    private val viewModel by viewModel<PlaylistInfoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistInfoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
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
        }
        viewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistInfoScreenEvent.NavigateBack -> findNavController().popBackStack()
            }
        }
        setupBackPressHandling()
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