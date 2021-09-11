package com.ayodeleochoa.ayoapps

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.ayodeleochoa.ayoapps.databinding.ActivityAccessibilityBinding

class AccessibilityActivity : AppCompatActivity()
{

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAccessibilityBinding

    val images = listOf(R.drawable.waterfall, R.drawable.beach, R.drawable.desert, R.drawable.new_york_city)
    val titles = listOf("Rainforest", "Beach", "Desert", "New York City")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccessibilityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val knowledgeFragment = DatabaseFragment()
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.knowledge_fragment, knowledgeFragment)
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .commit()

        val imageview = binding.ivLandscape
        val title = binding.txtTitle

        var position = 0;

        var currentImage = images.get(position)
        currentImage?.let { imageview.setImageResource(it) }

        var currentTitle = titles.get(position)
        println("currentTitle = " + currentTitle)
        title.text = currentTitle
        imageview.setContentDescription("Image ${position + 1} of ${images.count()}")

        val previousButton = binding.btnPrevious
        previousButton.setOnClickListener()
        {
            position--

            if (position < 0)
            {
                position = images.count() - 1;
            }

            imageview.setImageResource(images.get(position))
            title.text = titles.get(position)
            title.announceForAccessibility(titles.get(position))
            imageview.setContentDescription("Image ${position + 1} of ${images.count()}")
        }

        val nextButton = binding.btnNext
        nextButton.setOnClickListener()
        {
            position++

            if (position >= images.count())
            {
                position = 0;
            }

            imageview.setImageResource(images.get(position))
            title.text = titles.get(position)
            title.announceForAccessibility(titles.get(position))
            imageview.setContentDescription("Image ${position + 1} of ${images.count()}")

        }


    }


}