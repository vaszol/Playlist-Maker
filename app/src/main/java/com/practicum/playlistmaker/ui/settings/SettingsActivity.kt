package com.practicum.playlistmaker.ui.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.domain.Creator

@SuppressLint("UseSwitchCompatOrMaterialCode")
class SettingsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var settingsBinding: ActivitySettingsBinding
    private var creator = Creator.provideIntentActionInteractor()

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
        settingsBinding.settingShareBtn.setOnClickListener(this@SettingsActivity)
        settingsBinding.settingSupportBtn.setOnClickListener(this@SettingsActivity)
        settingsBinding.settingTermsBtn.setOnClickListener(this@SettingsActivity)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            settingsBinding.settingShareBtn -> creator.send(this)
            settingsBinding.settingSupportBtn -> creator.sendTo(this)
            settingsBinding.settingTermsBtn -> creator.view(this)
        }
    }
}