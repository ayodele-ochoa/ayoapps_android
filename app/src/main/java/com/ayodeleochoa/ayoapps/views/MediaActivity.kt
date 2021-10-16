package com.ayodeleochoa.ayoapps.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer
import com.ayodeleochoa.ayoapps.DatabaseFragment
import com.ayodeleochoa.ayoapps.R
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

        val knowledgeFragment = DatabaseFragment()
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.knowledge_fragment, knowledgeFragment)
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .commit()

      //  tabs.setupWithViewPager(viewPager)
        tabLayout.addTab(tabLayout.newTab().setText("VIDEO"))
        tabLayout.addTab(tabLayout.newTab().setText("AUDIO"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = MediaAdapter(this, supportFragmentManager,
            tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.setPageTransformer(true, CubeOutTransformer())
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab)
            {
                viewPager.currentItem = tab.position
                println("tab.position = " + tab.position)
                if (tab.position == 1)
                {
                 //   val tabOneFragment: VideoFragment = adapter.getItem(0) as VideoFragment
                  //  tabOneFragment.pauseVideo()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


    }

}

