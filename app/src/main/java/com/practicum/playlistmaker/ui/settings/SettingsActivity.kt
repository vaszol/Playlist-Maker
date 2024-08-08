package com.practicum.playlistmaker.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.ui.settings.viewModel.SettingsViewModel

@SuppressLint("UseSwitchCompatOrMaterialCode")
class SettingsActivity : AppCompatActivity() {
    private lateinit var settingsBinding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(settingsBinding.root)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]
        settingsBinding.apply {
            settingsToolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            settingThemeSwitch.apply {
                viewModel.theme.observe(this@SettingsActivity) { isChecked = it }
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