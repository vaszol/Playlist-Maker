package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class SettingsActivity : AppCompatActivity(), View.OnClickListener {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val back = findViewById<ImageView>(R.id.toolbar_back)
        back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

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
                val address = Uri.parse(getString(R.string.android_developer))
                val openlink = Intent(Intent.ACTION_VIEW, address)
                startActivity(openlink)
            }
            R.id.setting_support_btn -> {
                val email = Intent(Intent.ACTION_SEND)
                email.setType("plain/text");
                email.putExtra(Intent.EXTRA_EMAIL,R.string.EXTRA_EMAIL);
                email.putExtra(Intent.EXTRA_SUBJECT,R.string.EXTRA_SUBJECT);
                email.putExtra(Intent.EXTRA_TEXT,R.string.EXTRA_TEXT);
                startActivity(email)
            }
            R.id.setting_terms_btn -> {
                val address = Uri.parse(R.string.practicum_offer.toString())
                val openlink = Intent(Intent.ACTION_VIEW, address)
                startActivity(openlink)
            }
        }
    }
}