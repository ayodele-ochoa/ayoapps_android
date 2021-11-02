package com.ayodeleochoa.ayoapps.views

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ayodeleochoa.ayoapps.DatabaseFragment
import com.ayodeleochoa.ayoapps.R
import com.mukesh.image_processing.ImageProcessor


val REQUEST_IMAGE_CAPTURE = 1
    lateinit var ivPhoto: ImageView
    lateinit var imageFilter1: ImageView
    lateinit var imageFilter2: ImageView
    lateinit var imageFilter3: ImageView
    lateinit var imageFilter4: ImageView
    lateinit var imageFilter5: ImageView
    val imageProcessor = ImageProcessor()

    lateinit var invertFilter: Bitmap
    lateinit var fleaFilter: Bitmap
    lateinit var greyFilter: Bitmap
    lateinit var snowFilter: Bitmap
    lateinit var embossFilter: Bitmap

    class CameraActivity : AppCompatActivity()
    {
        override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        // Show knowledge fragment
        val knowledgeFragment = DatabaseFragment()
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.knowledge_fragment, knowledgeFragment)
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .commit()

        val buttonCamera = findViewById<ImageButton>(R.id.btnCamera)
        buttonCamera.setOnClickListener()
        {
            dispatchTakePictureIntent()
        }

        ivPhoto = findViewById(R.id.imgCamera)

        imageFilter1 = findViewById(R.id.imgFilter1)
        imageFilter1.setOnClickListener {
            ivPhoto.setImageBitmap(invertFilter)
        }

        imageFilter2 = findViewById(R.id.imgFilter2)
        imageFilter2.setOnClickListener {
            ivPhoto.setImageBitmap(fleaFilter)
        }

        imageFilter3 = findViewById(R.id.imgFilter3)
        imageFilter3.setOnClickListener {
            ivPhoto.setImageBitmap(greyFilter)
        }

        imageFilter4 = findViewById(R.id.imgFilter4)
        imageFilter4.setOnClickListener {
            ivPhoto.setImageBitmap(snowFilter)
        }

        imageFilter5 = findViewById(R.id.imgFilter5)
        imageFilter5.setOnClickListener {
            ivPhoto.setImageBitmap(embossFilter)
        }
    }

    private fun dispatchTakePictureIntent()
    {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try
        {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
        catch (e: ActivityNotFoundException)
        {
            println(e)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            ivPhoto.setImageBitmap(imageBitmap)


            invertFilter = imageProcessor.doInvert(imageBitmap)
            fleaFilter = imageProcessor.applyFleaEffect(imageBitmap)
            greyFilter = imageProcessor.doGreyScale(imageBitmap)
            snowFilter = imageProcessor.applySnowEffect(imageBitmap)
            embossFilter = imageProcessor.emboss(imageBitmap)

            imageFilter1.setImageBitmap(invertFilter)
            imageFilter2.setImageBitmap(fleaFilter)
            imageFilter3.setImageBitmap(greyFilter)
            imageFilter4.setImageBitmap(snowFilter)
            imageFilter5.setImageBitmap(embossFilter)

        }
        else
        {
            println("Error taking picture")
        }
    }


}

