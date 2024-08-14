package com.practicum.playlistmaker.ui.playlist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlaylistBinding

class PlaylistActivity : AppCompatActivity() {
    private lateinit var mediaBinding: ActivityPlaylistBinding
    private lateinit var tabMediator: TabLayoutMediator

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaBinding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(mediaBinding.root)

        mediaBinding.apply {
            mediaToolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            viewPager.adapter = MediaViewPagerAdapter(supportFragmentManager, lifecycle)

            tabMediator = TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
                tab.text = getString(
                    listOf(
                        R.string.favorite_title,
                        R.string.playlist_title
                    )[pos]
                )
            }
            tabMediator.attach()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}