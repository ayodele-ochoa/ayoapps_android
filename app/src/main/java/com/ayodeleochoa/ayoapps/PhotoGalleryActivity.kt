package com.ayodeleochoa.ayoapps

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.ui.AppBarConfiguration
import com.ayodeleochoa.ayoapps.api.ApiHelper
import com.ayodeleochoa.ayoapps.databinding.ActivityPhotoGalleryBinding
import com.ayodeleochoa.ayoapps.models.*
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView

//import kotlinx.android.synthetic.main.activity_user.progressBar
//import kotlinx.android.synthetic.main.activity_user.recyclerView

class PhotoGalleryActivity : AppCompatActivity()
{

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityPhotoGalleryBinding

    private lateinit var viewModel: PhotoMainViewModel
    private lateinit var adapter: SliderImageAdapter

    lateinit var sliderView: SliderView
    lateinit var progressBar: ProgressBar

    val photoList: ArrayList<Photo> = ArrayList()
    val imageList: ArrayList<String> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sliderView = binding.imageSlider
        progressBar = binding.progressBar

        val context = this

        val knowledgeFragment = DatabaseFragment()
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.knowledge_fragment, knowledgeFragment)
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .commit()

      //  setupViewModel()
        // setupUI()
      //  setupObservers()


    }

    private fun setImageInSlider(images: ArrayList<Photo>, imageSlider: SliderView)
    {
        val adapter = SliderImageAdapter()
        adapter.renewItems(images)
        imageSlider.setSliderAdapter(adapter)
        imageSlider.isAutoCycle = true
        imageSlider.scrollTimeInSec = 3
        imageSlider.setSliderTransformAnimation(SliderAnimations.CUBEINDEPTHTRANSFORMATION)
        imageSlider.startAutoCycle()
    }



    public fun setupViewModel()
    {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(PhotoMainViewModel::class.java)
    }

    /*private fun setupUI() {
        sliderView.layoutManager = LinearLayoutManager(this)
        adapter = PhotoMainAdapter(arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }*/

    public fun setupObservers() {
        viewModel.getPhotos().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        println("SUCCESS")
                        println("Data = " + resource.data)
                        sliderView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        for (item in resource.data!!)
                        {
                            photoList.add(item)
                        }
                        setImageInSlider(photoList, sliderView)
                      //  resource.data?.let { photos -> retrieveList(photos as ArrayList<Photo>) }
                    }
                    Status.ERROR -> {
                        println("ERROR: " + it.message)

                        sliderView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        println("LOADING")
                        progressBar.visibility = View.VISIBLE
                        sliderView.visibility = View.GONE
                    }
                }
            }
        })
    }

    /*private fun retrieveList(photos: ArrayList<Photo>) {
        adapter.apply {
            addPhotos(photos)
            notifyDataSetChanged()
        }
    }*/
}