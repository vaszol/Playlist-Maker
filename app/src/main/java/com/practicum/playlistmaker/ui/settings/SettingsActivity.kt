package com.practicum.playlistmaker.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding

@SuppressLint("UseSwitchCompatOrMaterialCode")
class SettingsActivity : AppCompatActivity() {
    private lateinit var settingsBinding: ActivitySettingsBinding

    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(settingsBinding.root)

        settingsBinding.settingsToolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        settingsBinding.settingThemeSwitch.setOnCheckedChangeListener(null)
        settingsBinding.settingThemeSwitch.isChecked =
            (applicationContext as App).getThemePreferences()
        settingsBinding.settingThemeSwitch.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }
        settingsBinding.settingShareBtn.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer))
                startActivity(this)
            }
        }
        settingsBinding.settingSupportBtn.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse(getString(R.string.mailto))
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.EXTRA_EMAIL)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.EXTRA_SUBJECT))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.EXTRA_TEXT))
                startActivity(this)
            }
        }
        settingsBinding.settingTermsBtn.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.practicum_offer))
                startActivity(this)
            }
        }
    }
}