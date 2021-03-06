package com.ayodeleochoa.ayoapps.models

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.ayodeleochoa.ayoapps.AccessibilityActivity
import com.ayodeleochoa.ayoapps.PhotoGalleryActivity
import com.ayodeleochoa.ayoapps.R
import com.ayodeleochoa.ayoapps.RoomDatabaseActivity
import com.ayodeleochoa.ayoapps.views.*

class MainAdapter(private val mList: List<MainViewModel>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {


    // create new views
    @SuppressLint("ResourceAsColor")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_card_view, parent, false)

        val displayMetrics: DisplayMetrics = parent.context.getResources().getDisplayMetrics()
        var screenWidth: Double = displayMetrics.widthPixels.toDouble()
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.width = (screenWidth / 2.5).toInt()
        layoutParams.height = layoutParams.width
        view.layoutParams = layoutParams

      //  view.setBackgroundColor(R.color.red)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {

        val ItemsViewModel = mList[position]
        val context: Context

        // sets the image to the imageview from our itemHolder class
        holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.textView.text = ItemsViewModel.text

        // OnClickListener
        holder.imageView.setOnClickListener(View.OnClickListener { v ->
            //  Toast.makeText(holder.imageView.context,"Position #{position}",Toast.LENGTH_SHORT).show()
            val activity = v.context as AppCompatActivity
            println("position $position clicked.")
            if (position == 0)
            {
                val intent = Intent(activity.applicationContext, RoomDatabaseActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.applicationContext.startActivity(intent)

            }
            else if (position == 1)
            {
                val intent = Intent(activity.applicationContext, AccessibilityActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.applicationContext.startActivity(intent)

            }
            else if (position == 2)
            {
                val intent = Intent(activity.applicationContext, PhotoGalleryActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.applicationContext.startActivity(intent)

            }
            else if (position == 3)
            {
                val intent = Intent(activity.applicationContext, GoogleMapsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.applicationContext.startActivity(intent)

            }
            else if (position == 4)
            {
            val intent = Intent(activity.applicationContext, BluetoothActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.applicationContext.startActivity(intent)

            }
            else if (position == 5)
            {
                val intent = Intent(activity.applicationContext, AdmobActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.applicationContext.startActivity(intent)

            }
            else if (position == 6)
            {
                val intent = Intent(activity.applicationContext, MediaActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.applicationContext.startActivity(intent)

            }
            else if (position == 7)
            {
                val intent = Intent(activity.applicationContext, AccelerometerActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.applicationContext.startActivity(intent)

            }
            else if (position == 9)
            {
                val intent = Intent(activity.applicationContext, CameraActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.applicationContext.startActivity(intent)

            }
        })


    }

    // return the number of the items in the list
    override fun getItemCount(): Int
    {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView)
    {
        val imageView: ImageView = itemView.findViewById(R.id.main_imageview)
        val textView: TextView = itemView.findViewById(R.id.main_textView)
    }
}