package com.ayodeleochoa.ayoapps.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayodeleochoa.ayoapps.R


class PhotoMainAdapter(private val photos: ArrayList<Photo>) : RecyclerView.Adapter<PhotoMainAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(photo: Photo) {
            itemView.apply {
                println("Photo Title: " + photo.title)
                println("Photo ID: " + photo.id)
                println("Photo URL: " + photo.url)
                /*textViewTitle.text = photo.title

                Glide.with(imageViewAvatar.context)
                    .load(user.avatar)
                    .into(imageViewAvatar)*/
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.photo_row_item, parent, false))

    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    /*fun addPhotos(photos: ArrayList<Photo>) {
        this.photos.apply {
            clear()
            addAll(photos)
        }

    }*/
}