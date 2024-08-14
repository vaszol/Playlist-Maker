package com.practicum.playlistmaker.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.ui.playlist.PlaylistActivity
import com.practicum.playlistmaker.ui.search.SearchActivity
import com.practicum.playlistmaker.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.search.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
        mainBinding.media.setOnClickListener {
            startActivity(Intent(this, PlaylistActivity::class.java))
        }
        mainBinding.settings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}