package com.practicum.playlistmaker.ui.playlist.favorite

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoriteBinding
import com.practicum.playlistmaker.ui.playlist.favorite.viewModel.FavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel by viewModel<FavoriteViewModel>()

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater)
        binding.apply {
            messageText.text = getString(R.string.favorite_empty)
            messageImg.setImageResource(
                if (Configuration.UI_MODE_NIGHT_YES == resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) R.drawable.ic_message_empty_dark
                else R.drawable.ic_message_empty
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.tracks.observe(viewLifecycleOwner) {
            binding.messageImg.isVisible = it.isEmpty()
            binding.messageText.isVisible = it.isEmpty()
        }
    }
}