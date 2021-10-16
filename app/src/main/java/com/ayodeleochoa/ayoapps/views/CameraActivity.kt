package com.ayodeleochoa.ayoapps.views

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.ayodeleochoa.ayoapps.R

    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var ivPhoto: ImageView

    class CameraActivity : AppCompatActivity()
    {
        override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        val buttonCamera = findViewById<ImageButton>(R.id.btnCamera)
        buttonCamera.setOnClickListener()
        {
            dispatchTakePictureIntent()
        }

        ivPhoto = findViewById<ImageView>(R.id.imgCamera)
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            ivPhoto.setImageBitmap(imageBitmap)
        }
    }


}