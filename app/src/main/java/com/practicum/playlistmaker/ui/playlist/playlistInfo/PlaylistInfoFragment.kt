package com.practicum.playlistmaker.ui.playlist.playlistInfo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.practicum.playlistmaker.ui.playlist.playlistInfo.viewModel.PlaylistInfoViewModel
import com.practicum.playlistmaker.ui.search.TracksAdapter
import com.practicum.playlistmaker.ui.setSource
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.hideNavigation()
        adapter = TracksAdapter(viewModel::onTrackClick, viewModel::onLongClick)
        binding.apply {
            playlists.adapter = adapter
            infoToolbar.setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
            btnShare.setOnClickListener { viewModel.onShareButtonClicked() }
            btnMenu.setOnClickListener { viewModel.onMenuButtonClicked() }
            overlay.setOnClickListener { viewModel.onOverlayClicked() }
            btnShareMenu.setOnClickListener { viewModel.onBtnShareMenuClicked() }
            btnEditMenu.setOnClickListener { viewModel.onBtnEditMenuClicked() }
            btnDeleteMenu.setOnClickListener { viewModel.onBtnDeleteMenuClicked() }
            BottomSheetBehavior.from(bottomSheetMenu).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
                addBottomSheetCallback(
                    object : BottomSheetBehavior.BottomSheetCallback() {
                        override fun onStateChanged(bottomSheetMenu: View, newState: Int) {
                            overlay.isVisible = newState != BottomSheetBehavior.STATE_HIDDEN
                            bottomSheet.isVisible = newState == BottomSheetBehavior.STATE_HIDDEN
                        }

                        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                    }
                )
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    state?.let {
                        val minuteText =
                            "${it.minute} ${
                                resources.getQuantityString(
                                    R.plurals.minute,
                                    it.minute
                                )
                            }"
                        val countText =
                            "${it.tracks.size} ${
                                resources.getQuantityString(R.plurals.track, it.tracks.size)
                            }"
                        binding.apply {
                            it.coverUri?.let { it1 -> trackImg.setSource(it1) }
                            it.coverUri?.let { it1 -> trackImgMenu.setSource(it1) }
                            name.text = it.name
                            nameMenu.text = it.name
                            description.text = it.description
                            total.text =
                                getString(R.string.playlist_info_total, minuteText, countText)
                            countMenu.text = countText
                            btnShareMenu.text = getString(R.string.playlist_menu_share)
                            btnEditMenu.text = getString(R.string.playlist_menu_edit)
                            btnDeleteMenu.text = getString(R.string.playlist_menu_delete)
                        }
                        adapter.updateList(it.tracks)
                    }
                }
            }
        }
        viewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                PlaylistInfoScreenEvent.NavigateBack -> findNavController().popBackStack()
                PlaylistInfoScreenEvent.OpenPlayerScreen -> findNavController().navigate(R.id.action_playlistInfoFragment_to_playerFragment)
                PlaylistInfoScreenEvent.ShowDeleteTrackConfirmationDialog -> showDeleteTrackConfirmationDialog()
                PlaylistInfoScreenEvent.ShowEmptyPlaylistDialog -> showEmptyPlaylistDialog()
                PlaylistInfoScreenEvent.SharePlaylist -> sharePlaylist()
                is PlaylistInfoScreenEvent.ShowMenu -> showMenu(it.isVisibleMenu)
                PlaylistInfoScreenEvent.ShowDeletePlaylistConfirmationDialog -> showDeletePlaylistConfirmationDialog()
                PlaylistInfoScreenEvent.OpenEditPlaylistScreen -> findNavController().navigate(R.id.action_playlistInfoFragment_to_createFragment)
            }
        }
        setupBackPressHandling()
    }

    private fun showDeleteTrackConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.playlist_info_delete_track_dialog_title)
            .setNegativeButton(R.string.playlist_info_delete_track_dialog_negative) { _, _ -> viewModel.onDeleteTrackDialogDismiss() }
            .setPositiveButton(R.string.playlist_info_delete_track_dialog_positive) { _, _ -> viewModel.onDeleteTrackConfirmed() }
            .show()
    }

    private fun showEmptyPlaylistDialog() {
        Toast.makeText(
            requireContext(),
            getString(R.string.playlist_Empty_message),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun sharePlaylist() {
        viewModel.tracks.value.let { tracks ->
            val countText =
                "${tracks.size} ${resources.getQuantityString(R.plurals.track, tracks.size)}"
            val infoText = tracks.mapIndexed { index, track ->
                val trackTime =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
                "${index + 1}. ${track.artistName} - ${track.trackName} (${trackTime})"
            }.joinToString("\n")
            viewModel.playlist.value?.let { playlist ->
                val message = "${playlist.name}\n${playlist.description}\n$countText\n$infoText"
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, message)
                    startActivity(this)
                }
            }
        }
    }

    private fun showMenu(isVisibleMenu: Boolean) {
        binding.bottomSheetMenu.let {
            val bottomSheetBehavior = BottomSheetBehavior.from(it)
            bottomSheetBehavior.state = if (isVisibleMenu)
                BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun showDeletePlaylistConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(
                getString(
                    R.string.playlist_info_delete_playlist_dialog_title,
                    viewModel.playlist.value?.name
                )
            )
            .setNegativeButton(R.string.playlist_info_delete_playlist_dialog_negative) { _, _ -> viewModel.onDeletePlaylistDialogDismiss() }
            .setPositiveButton(R.string.playlist_info_delete_playlist_dialog_positive) { _, _ -> viewModel.onDeletePlaylistConfirmed() }
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