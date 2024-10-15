package com.practicum.playlistmaker.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.domain.models.PlayerStateEnum
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.setSource
import com.practicum.playlistmaker.ui.util.ResultKeyHolder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {

    private lateinit var binding: FragmentPlayerBinding
    private val viewModel by viewModel<PlyaerViewModel>()
    private lateinit var bottomAdapter: BottomAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(layoutInflater)
        requireActivity().supportFragmentManager.setFragmentResultListener(
            ResultKeyHolder.KEY_PLAYLIST_CREATED,
            viewLifecycleOwner
        ) { _, bundle ->
            bundle.getString(ResultKeyHolder.KEY_PLAYLIST_NAME)?.let {
                val text = getString(R.string.playlist_created_snackbar, it)
                showToast(text)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.hideNavigation()
        viewModel.setTrack()
        viewModel.state.observe(this) {
            it.track?.let(::setTrackData)
            binding.trackTime.text = it.trackTime.ifEmpty { "00:00" }
            it.playerState.let { playerStateEnum: PlayerStateEnum ->
                when (playerStateEnum) {
                    PlayerStateEnum.STATE_PLAYING -> binding.playBtn.setImageResource(R.drawable.ic_pause)
                    PlayerStateEnum.STATE_PAUSED -> binding.playBtn.setImageResource(R.drawable.ic_play)
                }
            }
            if (it.isLiked)
                binding.likeBtn.setImageResource(R.drawable.ic_like)
            else
                binding.likeBtn.setImageResource(R.drawable.ic_not_like)
        }
        binding.apply {
            mediaToolbar.setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
            addBtn.setOnClickListener { viewModel.add() }
            playBtn.setOnClickListener { viewModel.play() }
            likeBtn.setOnClickListener { viewModel.like() }
            BottomSheetBehavior.from(bottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
                addBottomSheetCallback(
                    object : BottomSheetBehavior.BottomSheetCallback() {
                        override fun onStateChanged(bottomSheet: View, newState: Int) {
                            overlay.isVisible = newState != BottomSheetBehavior.STATE_HIDDEN
                        }

                        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                    })
            }
            create.setOnClickListener { viewModel.onCreateClicker() }
            bottomAdapter = BottomAdapter(viewModel::onPlaylistClicked)
            playlists.adapter = bottomAdapter
        }
        viewModel.playlists.observe(this) { bottomAdapter.submitList(it) }
        viewModel.event.observe(this) { it ->
            when (it) {
                is PlayerScreenEvent.OpenBottomSheet -> {
                    binding.bottomSheet.let {
                        val bottomSheetBehavior = BottomSheetBehavior.from(it)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }

                is PlayerScreenEvent.CloseBottomSheet -> {
                    binding.bottomSheet.let {
                        val bottomSheetBehavior = BottomSheetBehavior.from(it)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                }

                is PlayerScreenEvent.ShowTrackAddedMessage -> {
                    showToast(getString(R.string.playlist_added, it.playlistName))
                }

                is PlayerScreenEvent.ShowTrackAlreadyInPlaylistMessage -> {
                    showToast(getString(R.string.playlist_already_added, it.playlistName))
                }

                is PlayerScreenEvent.NavigateToCreatePlaylistScreen -> {
                    viewModel.hideNavigation()
                    findNavController().navigate(R.id.action_playerFragment_to_createFragment)
                }
            }
        }
        setupBackPressHandling()
    }

    private fun setupBackPressHandling() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isEnabled) {
                    viewModel.showNavigation()
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun setTrackData(track: Track) {
        binding.apply {
            trackImg.setSource(track.getCoverArtwork())
            trackName.text = track.trackName
            trackArtist.text = track.artistName
            infoTrackCollectionGroup.isVisible = track.collectionName.isNotEmpty()
            track.collectionName.also { infoTrackCollectionVal.text = it }
            infoTrackReleaseDateVal.text = track.getYear()
            infoTrackGenreVal.text = track.primaryGenreName
            infoTrackCountryVal.text = track.country
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stop()
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}