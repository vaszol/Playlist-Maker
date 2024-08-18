package com.practicum.playlistmaker.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.ui.settings.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("UseSwitchCompatOrMaterialCode")
class SettingsFragment : Fragment() {
    private lateinit var settingsBinding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsBinding = FragmentSettingsBinding.inflate(layoutInflater)
        return settingsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingsBinding.apply {
            settingThemeSwitch.apply {
                viewModel.theme.observe(viewLifecycleOwner) { isChecked = it }
                setOnClickListener { viewModel.themeSwitch(this.isChecked) }
            }
            settingShareBtn.setOnClickListener {
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer))
                    startActivity(this)
                }
            }
            settingSupportBtn.setOnClickListener {
                Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse(getString(R.string.mailto))
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.EXTRA_EMAIL)))
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.EXTRA_SUBJECT))
                    putExtra(Intent.EXTRA_TEXT, getString(R.string.EXTRA_TEXT))
                    startActivity(this)
                }
            }
            settingTermsBtn.setOnClickListener {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(getString(R.string.practicum_offer))
                    startActivity(this)
                }
            }
        }
    }
}