package com.ayodeleochoa.ayoapps

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayodeleochoa.ayoapps.models.MainAdapter
import com.ayodeleochoa.ayoapps.models.MainViewModel





class MainActivity : AppCompatActivity()
{
    val mapImagesandTitles: Map<Int, String> = mapOf(
        R.drawable.database to "SQL Lite",
        R.drawable.magnifying_glass to "Assessibility",
        R.drawable.image_gallery to "Image Gallery",
        R.drawable.map to "Google Maps",
        R.drawable.bluetooth to "Bluetooth",
        R.drawable.ads to "Advertisement",
        R.drawable.audio_video to "Media Players",
        R.drawable.accelorometer to "Motion Sensors",
        R.drawable.notification to "Notifications",
        R.drawable.camera to "Camera"

    )
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = GridLayoutManager(applicationContext, 2)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<MainViewModel>()

        // the image with the count of view
        mapImagesandTitles.forEach()
        {
                image, title -> data.add(MainViewModel(image, title))
        }
        /*for (i in 1..8)
        {
            data.add(MainViewModel(R.drawable.database, "Item " + i))
        }*/

        // This will pass the ArrayList to our Adapter
        val adapter = MainAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter


    }

}