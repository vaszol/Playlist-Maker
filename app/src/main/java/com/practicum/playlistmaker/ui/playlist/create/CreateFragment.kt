package com.practicum.playlistmaker.ui.playlist.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.databinding.FragmentCreateBinding
import com.practicum.playlistmaker.ui.load
import com.practicum.playlistmaker.ui.playlist.create.viewModel.CreateViewModel
import com.practicum.playlistmaker.ui.util.ResultKeyHolder
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateFragment : Fragment() {
    private lateinit var binding: FragmentCreateBinding

    private val viewModel by viewModel<CreateViewModel>()
    private val pickMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {
        it?.let { uri -> viewModel.onPlaylistCoverSelected(uri) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            createToolbar.setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
            photoPicker.setOnClickListener {
                pickMedia.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
            name.doAfterTextChanged { viewModel.onNameChanged(it.toString()) }
            description.doAfterTextChanged { viewModel.onDescriptionChanged(it.toString()) }
            btnComplete.setOnClickListener { viewModel.onCreateBtnClick() }
        }
        viewModel.isCreateBtnEnabled.observe(viewLifecycleOwner) {
            binding.btnComplete.isEnabled = it
        }
        viewModel.playlistCoverUri.observe(viewLifecycleOwner) {
            it.let { binding.photoPicker.load(it.toString()) }
        }
        viewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                is CreateEvent.NavigateBack -> findNavController().popBackStack()
                is CreateEvent.ShowBackConfirmationDialog -> showConfirmDialog()
                is CreateEvent.SetPlaylistCreatedResult -> {
                    requireActivity().supportFragmentManager.setFragmentResult(
                        ResultKeyHolder.KEY_PLAYLIST_CREATED,
                        bundleOf(ResultKeyHolder.KEY_PLAYLIST_NAME to it.playlistName)
                    )
                }
            }
        }
        setupBackPressHandling()
    }

    private fun showConfirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNegativeButton("Отмена") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Завершить") { _, _ -> viewModel.onBackPressedConfirmed() }
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