package com.ayodeleochoa.ayoapps.models

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ayodeleochoa.ayoapps.views.AudioFragment
import com.ayodeleochoa.ayoapps.views.VideoFragment

@Suppress("DEPRECATION")
internal class MediaAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int
) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                VideoFragment()
            }
            1 -> {
                AudioFragment()
            }
            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}