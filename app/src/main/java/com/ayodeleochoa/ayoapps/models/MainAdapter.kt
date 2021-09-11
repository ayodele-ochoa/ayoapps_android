package com.ayodeleochoa.ayoapps.models

import android.content.Context
import android.content.Intent
import android.graphics.Color
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

 class MainAdapter(private val mList: List<MainViewModel>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {


    // create new views
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

        view.setBackgroundColor(Color.LTGRAY)
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
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val textView: TextView = itemView.findViewById(R.id.textView)
    }
}