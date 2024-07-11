package com.practicum.playlistmaker.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.ui.media.MediaActivity
import com.practicum.playlistmaker.ui.search.SearchActivity
import com.practicum.playlistmaker.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.search.setOnClickListener(this@MainActivity)
        mainBinding.media.setOnClickListener(this@MainActivity)
        mainBinding.settings.setOnClickListener(this@MainActivity)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            mainBinding.search -> startActivity(Intent(this, SearchActivity::class.java))
            mainBinding.media -> startActivity(Intent(this, MediaActivity::class.java))
            mainBinding.settings -> startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}