package com.ayodeleochoa.ayoapps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DatabaseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DatabaseFragment : Fragment()
{

    var viewedDescription = false
    var currentActivity = activity?.javaClass?.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Instead of view.findViewById(R.id.hello) as TextView
       // hello?.setText("Hello, world!")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_knowledge, container, false)

        /*val sharedPreference = activity?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        var editor = sharedPreference?.edit()
        if (sharedPreference != null) {
            viewedDescription = sharedPreference.getBoolean("viewedDescription", false)
        }*/
        if (viewedDescription == false) {


            currentActivity = activity?.javaClass?.simpleName
            println("currentActivity = " + currentActivity)

            val txtDescription = view.findViewById(R.id.txtDescription) as TextView
            val txtDescription2 = view.findViewById(R.id.txtDescription2) as TextView

            if (currentActivity == "RoomDatabaseActivity") {

                val descriptionText =
                    "This exercise saves persistent data onto a Room (SQLite) database. " +
                            "Enter in the new student's first and last name, along with their grade and then click add student." +
                            "The new student will be added to the database and the listview will be updated to reflect the current" +
                            "list of students. Click on clear database to reset the database."
                val descriptionText2 = "This exercise include the following features: \n" +
                        "Room (SQLite) persistent data \n" +
                        "Data Binding \n" +
                        "Listview \n" +
                        "Fragments \n" +
                        "Edittext \n" +
                        "Buttons"
                txtDescription.text = descriptionText
                txtDescription2.text = descriptionText2
            }
            else if (currentActivity == "AccessibilityActivity") {
                val descriptionText =
                    "This exercise illustrates implementations of accessibility features " +
                            "to make the app easier to navigate for those with possible vision and/or motor impairments. " +
                            "Click on each element to receive a voiced description. The descriptions are updated dynamically " +
                            "as the activity is updated."
                val descriptionText2 =
                    "This activity was tested by Google's Accessibility Scanner to ensure acceptable " +
                            "accessibility. To receive voice feedback from the activity you must first navigate to " +
                            "Settings/Accessibility and turn on the Talkback feature. "
                txtDescription.text = descriptionText
                txtDescription2.text = descriptionText2
            }
            else if (currentActivity == "PhotoGalleryActivity") {
                val descriptionText =
                    "This exercise displays a photo gallery within an automatically sliding view. The url's and titles " +
                            "are stored in a remote json file that is retrieved via an api rest request. Swipe left or right " +
                            "to manually switch images"
                val descriptionText2 =
                    "This exercise include the following features: \n \n" +
                            "Kotlin Coroutine \n" +
                            "Retrofit \n" +
                            "Squareup Picasso \n" +
                            "Smartiest AutoImageSlider \n" +
                            "Progress Bar \n" +
                            "Fragments"
                txtDescription.text = descriptionText
                txtDescription2.text = descriptionText2
            }
            else if (currentActivity == "GoogleMapsActivity") {
                val descriptionText =
                    "This exercise displays the use of the Google Maps SDK for Android, Google Places and the Google " +
                            "directions API. On the load of activity, after permission is granted, the app will locate " +
                            "and display your current location on the Google Map. Enter the name or address of a " +
                            "location into the EditText box and the directions from your location to the entered " +
                            "destination will be displayed on the map, along with the distance to that destination.  "
                val descriptionText2 =
                    "This exercise include the following features: \n \n" +
                            "Google Maps SDK \n" +
                            "Google Places \n" +
                            "Google Directions API \n" +
                            "Android Location \n" +
                            "Android Permissions \n" +
                            "AsyncTask \n" +
                            "EditText \n" +
                            "Fragments"
                txtDescription.text = descriptionText
                txtDescription2.text = descriptionText2
            }

            viewedDescription = true


            val gotItButton = view.findViewById(R.id.btnGotItDB) as Button
            gotItButton.setOnClickListener {

                if (currentActivity == "PhotoGalleryActivity")
                {
                    (activity as PhotoGalleryActivity?)!!.setupViewModel()
                    (activity as PhotoGalleryActivity?)!!.setupObservers()
                }

                val fragment = fragmentManager?.findFragmentById(R.id.knowledge_fragment)
                if (fragment != null) {
                    val fragmentTransaction = fragmentManager?.beginTransaction()
                    fragmentTransaction?.remove(fragment)
                    fragmentTransaction?.commit()
                }
            }


            /*editor?.putBoolean("viewedDescription",true)
            editor?.apply()*/
        }



        return  view

    }

    override fun onDestroy()
    {
        super.onDestroy()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DatabaseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DatabaseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}