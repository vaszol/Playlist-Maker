package com.practicum.playlistmaker.ui.playlist.favorite

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoriteBinding
import com.practicum.playlistmaker.ui.playlist.favorite.viewModel.FavoriteViewModel
import com.practicum.playlistmaker.ui.search.TracksAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: TracksAdapter
    private val viewModel by viewModel<FavoriteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.showNavigation()
        adapter = TracksAdapter(viewModel::onTrackClick)
        binding.apply {
            trackRecyclerView.adapter = adapter
            messageText.text = getString(R.string.favorite_empty)
            messageImg.setImageResource(
                if (Configuration.UI_MODE_NIGHT_YES == resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) R.drawable.ic_message_empty_dark
                else R.drawable.ic_message_empty
            )
        }
        viewModel.tracks.observe(viewLifecycleOwner) {
            binding.apply {
                adapter.updateList(it)
            }
            binding.messageImg.isVisible = it.isEmpty()
            binding.messageText.isVisible = it.isEmpty()
        }

        viewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                FavoriteScreenEvent.OpenPlayerScreen -> findNavController().navigate(R.id.action_mediaFragment_to_playerFragment)
            }
        }
    }

    companion object {
        fun newInstance() = FavoriteFragment()
    }
}