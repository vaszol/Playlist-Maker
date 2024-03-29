package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


class SettingsActivity : AppCompatActivity(), View.OnClickListener {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.settings_toolbar)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val settingShare = findViewById<ImageView>(R.id.setting_share_btn)
        settingShare.setOnClickListener(this@SettingsActivity)

        val settingSupport = findViewById<ImageView>(R.id.setting_support_btn)
        settingSupport.setOnClickListener(this@SettingsActivity)

        val settingTerms = findViewById<ImageView>(R.id.setting_terms_btn)
        settingTerms.setOnClickListener(this@SettingsActivity)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.setting_share_btn -> {
                Intent(Intent.ACTION_SEND).apply {
                    type = "plain/text"
                    putExtra(Intent.EXTRA_TEXT,Uri.parse(getString(R.string.android_developer)))
                    startActivity(this)
                }
            }
            R.id.setting_support_btn -> {

                Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse(getString(R.string.mailto))
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.EXTRA_EMAIL)))
                    putExtra(Intent.EXTRA_SUBJECT,getString(R.string.EXTRA_SUBJECT))
                    putExtra(Intent.EXTRA_TEXT,getString(R.string.EXTRA_TEXT))
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