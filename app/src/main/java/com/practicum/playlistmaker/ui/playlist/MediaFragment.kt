package com.practicum.playlistmaker.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMediaBinding

class MediaFragment : Fragment() {
    private lateinit var mediaBinding: FragmentMediaBinding
    private lateinit var tabMediator: TabLayoutMediator
    private val titles = listOf(
        R.string.favorite_title,
        R.string.playlist_title
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mediaBinding = FragmentMediaBinding.inflate(layoutInflater)
        return mediaBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaBinding.apply {
            viewPager.adapter = MediaViewPagerAdapter(childFragmentManager, lifecycle)
            tabMediator = TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
                tab.text = getString(titles[pos])
            }
            tabMediator.attach()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}