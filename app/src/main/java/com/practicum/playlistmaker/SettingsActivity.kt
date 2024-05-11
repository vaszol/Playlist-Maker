package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


@SuppressLint("UseSwitchCompatOrMaterialCode")
class SettingsActivity : AppCompatActivity(), View.OnClickListener {
    private var toolbar: Toolbar? = null
    private var themeSwitcher: Switch? = null
    private var settingShare: ImageView? = null
    private var settingSupport: ImageView? = null
    private var settingTerms: ImageView? = null

    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolbar = findViewById(R.id.settings_toolbar)
        themeSwitcher = findViewById(R.id.setting_theme_switch)
        settingShare = findViewById(R.id.setting_share_btn)
        settingSupport = findViewById(R.id.setting_support_btn)
        settingTerms = findViewById(R.id.setting_terms_btn)

        toolbar?.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        themeSwitcher?.setOnCheckedChangeListener(null);
        themeSwitcher?.setChecked((applicationContext as App).getThemePreferences());
        themeSwitcher?.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }
        settingShare?.setOnClickListener(this@SettingsActivity)
        settingSupport?.setOnClickListener(this@SettingsActivity)
        settingTerms?.setOnClickListener(this@SettingsActivity)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.setting_share_btn -> {
                Intent(Intent.ACTION_SEND).apply {
                    type = "plain/text"
                    putExtra(Intent.EXTRA_TEXT, Uri.parse(getString(R.string.android_developer)))
                    startActivity(this)
                }
            }

            R.id.setting_support_btn -> {

                Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse(getString(R.string.mailto))
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.EXTRA_EMAIL)))
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.EXTRA_SUBJECT))
                    putExtra(Intent.EXTRA_TEXT, getString(R.string.EXTRA_TEXT))
                    startActivity(this)
                }
            }

            R.id.setting_terms_btn -> {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(getString(R.string.practicum_offer))
                    startActivity(this)
                }
            }
        }
    }
}