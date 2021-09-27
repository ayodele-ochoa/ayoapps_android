package com.ayodeleochoa.ayoapps.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.ayodeleochoa.ayoapps.databinding.ActivityMediaBinding
import com.ayodeleochoa.ayoapps.models.MediaAdapter
import com.google.android.material.tabs.TabLayout

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

      //  val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
       // viewPager.adapter = sectionsPagerAdapter
        val tabLayout: TabLayout = binding.tabs
      //  tabs.setupWithViewPager(viewPager)
        tabLayout.addTab(tabLayout.newTab().setText("VIDEO"))
        tabLayout.addTab(tabLayout.newTab().setText("AUDIO"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = MediaAdapter(this, supportFragmentManager,
            tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


    }

}