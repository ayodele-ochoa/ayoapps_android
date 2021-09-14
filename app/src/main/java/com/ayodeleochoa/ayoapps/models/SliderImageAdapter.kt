package com.ayodeleochoa.ayoapps.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ayodeleochoa.ayoapps.R
import com.smarteist.autoimageslider.SliderViewAdapter
import com.squareup.picasso.Picasso
import java.util.*

class SliderImageAdapter() :
    SliderViewAdapter<SliderImageAdapter.VH>() {
    private var sliderPhotos = ArrayList<Photo>()

    fun renewItems(sliderItems: ArrayList<Photo>) {
        sliderPhotos = sliderItems
        notifyDataSetChanged()
    }

    /*fun addPhotos(photos: ArrayList<Photo>) {
        this.photos.apply {
            clear()
            addAll(photos)
        }

    }*/

    fun addItem(sliderItem: Photo) {
        sliderPhotos.add(sliderItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): VH {
        val inflate: View = LayoutInflater.from(parent.context).inflate(R.layout.image_slider_layout, null)
        return VH(inflate)
    }

    override fun onBindViewHolder(viewHolder: VH, position: Int) {
        //load image into view
        println("Current url = " + sliderPhotos[position].url)
        Picasso.get().load(sliderPhotos[position].url).fit().into(viewHolder.imageview)
        viewHolder.textview.text = sliderPhotos[position].title

    }

    override fun getCount(): Int {
        return sliderPhotos.size
    }

    inner class VH(itemView: View) : ViewHolder(itemView) {
        var imageview: ImageView = itemView.findViewById(R.id.iv_auto_image_slider)
        var textview: TextView = itemView.findViewById(R.id.tv_auto_image_slider)
    }
}