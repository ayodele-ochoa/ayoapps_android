package com.ayodeleochoa.ayoapps.views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ayodeleochoa.ayoapps.DatabaseFragment
import com.ayodeleochoa.ayoapps.R
import com.ayodeleochoa.ayoapps.databinding.ActivityGoogleMapsBinding
import com.ayodeleochoa.ayoapps.models.DataParser
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityGoogleMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val PERMISSION_REQUEST_CODE = 42
    lateinit var currentLocation: Location

    var dest_latitude = 0.0
    var dest_longitude = 0.0
    var start_latitude = 0.0
    var start_longitude = 0.0
    var origin: MarkerOptions? = null
    var destination: MarkerOptions? = null
    var lastPolyLine: Polyline? = null
    var oldmarker: Marker? = null

    // var infoText: TextView? = null
    var destinationEdit: EditText? = null

    val google_api_key: String = "AIzaSyB3j7dvDDgSpU4dTr5eOp1oyIqo_mfW3mo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val knowledgeFragment = DatabaseFragment()
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.knowledge_fragment, knowledgeFragment)
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .commit()

        binding = ActivityGoogleMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val buttonSearch = binding.btnSearch
        destinationEdit = binding.editDestination

        buttonSearch.setOnClickListener {
            // getLocation()
            searchArea()
        }

        destinationEdit?.setOnKeyListener { v, keyCode, event ->
            when {
                //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {
                    println("destinationEdit clicked...")
                    buttonSearch.performClick()

                    //return true
                    return@setOnKeyListener true
                }
                else -> false
            }
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun searchArea() {
        val infoText = binding.txtInfo

        val location = destinationEdit!!.text.toString()

        // Remove previous marker and polyline before setting new ones
        mMap.clear()
        if (currentLocation != null)
        {
            mMap.addMarker(MarkerOptions().position(LatLng(
                currentLocation.latitude,
                currentLocation.longitude
            )).title("Your Location"))

        }

        var addressList: List<Address>? = null

        val markerOptions = MarkerOptions()

        println("location = " + location)
        if (location != "") {
            val geocoder = Geocoder(applicationContext)
            try {
                addressList = geocoder.getFromLocationName(location, 5)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (addressList != null) {
                for (i in addressList.indices) {
                    val myAddress = addressList[i]
                    val latlng = LatLng(myAddress.latitude, myAddress.longitude)
                    markerOptions.position(latlng)
                    mMap!!.addMarker(markerOptions)
                    dest_latitude = myAddress.latitude
                    dest_longitude = myAddress.longitude
                    mMap!!.animateCamera(CameraUpdateFactory.newLatLng((latlng)))

                    val mo = MarkerOptions()
                    mo.title("Distance")
                    val results = FloatArray(10)
                    Location.distanceBetween(
                        currentLocation.latitude,
                        currentLocation.longitude,
                        dest_latitude,
                        dest_longitude,
                        results
                    )

                    //  val s = String.format("%.1f", results[0] / 1000)
                    val distanceMiles = (results[0] * .000621371)
                    val strDistanceMiles = String.format("%.2f", (results[0] * .000621371))

                    // Setting marker to draw route between these two points
                    origin = MarkerOptions().position(
                        (LatLng(
                            currentLocation.latitude,
                            currentLocation.longitude
                        ))
                    )
                        .title("HSR Layout").snippet("origin")
                    destination = MarkerOptions().position((LatLng(dest_latitude, dest_longitude)))
                        .title(destinationEdit!!.text.toString())
                        .snippet("Distance = $strDistanceMiles miles")

                    oldmarker = mMap!!.addMarker((destination))
                    //   Toast.makeText(this@GoogleMapsActivity, "Distance = $strDistanceMiles miles", Toast.LENGTH_SHORT).show()



                    val padding = 60
                    val builder = LatLngBounds.Builder()
                    builder.include(LatLng(currentLocation.latitude, currentLocation.longitude))
                    builder.include(LatLng(dest_latitude, dest_longitude))
                    val bounds = builder.build()
                  //  final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    val cameraupdate: CameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                    mMap.setOnMapLoadedCallback {
                        /**set animated zoom camera into map */
                        /**set animated zoom camera into map */
                        mMap.animateCamera(cameraupdate)
                    }

                    infoText.setText("Distance = $strDistanceMiles miles away.")
                    destinationEdit!!.clearFocus()
                    destinationEdit!!.text?.clear()
                    destinationEdit!!.hideKeyboard()


                    val url: String = getDirectionsUrl(origin!!.position, destination!!.position)

                    val downloadTask: DownloadTask = DownloadTask()
                    downloadTask.execute(url)
                }
            }
        }
    }

    private fun getDirectionsUrl(origin: LatLng, dest: LatLng): String
    {
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude
        val mode = "mode=driving"
        val parameters = "$str_origin&$str_dest&$mode"
        val output = "json"

        val finalApiString = "https://maps.googleapis.com/maps/api/directions/$output?$parameters&key=$google_api_key"
        println("finalApiString = " + finalApiString)

        return  finalApiString
    }

    inner class DownloadTask: AsyncTask<String?, Void?, String>()
    {
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val parserTask = ParserTask()
            parserTask.execute(result)
        }

        override fun doInBackground(vararg url: String?): String {
            var data = ""
            try
            {
                data = downloadUrl(url[0].toString()).toString()
                println("downloadTask data = " + data)
            }
            catch (e: java.lang.Exception)
            {
                println("Background Task ${e.toString()}")
            }
            return data
        }


        inner class ParserTask: AsyncTask<String?, Int?, List<List<HashMap<String, String>>>?>()
        {
            override fun doInBackground(vararg jsonData: String?): List<List<HashMap<String, String>>>? {
                val jObject: JSONObject
                var routes: List<List<HashMap<String, String>>>? = null

                try {
                    jObject = JSONObject(jsonData[0])
                    val parser = DataParser()
                    routes = parser.parse(jObject)
                    println("routes = " + routes)
                }
                catch(e: java.lang.Exception) {
                   e.printStackTrace()
                }
                return routes
            }

            override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
              //  super.onPostExecute(result)

                val points = ArrayList<LatLng?>()
                val lineOptions = PolylineOptions()
                for (i in result!!.indices)
                {
                    val path = result[i]
                    for (j in path.indices)
                    {
                        val point = path[j]
                        val lat = point["lat"]!!.toDouble()
                        val lng = point["lng"]!!.toDouble()
                        val position = LatLng(lat, lng)
                        points.add(position)
                    }

                    lineOptions.addAll(points)
                    lineOptions.width(9f)
                    lineOptions.color(Color.BLUE)
                    lineOptions.geodesic(true)
                }

                if (points.size != 0)
                {

                 lastPolyLine = mMap!!.addPolyline(lineOptions)
                }
            }



        }

        private fun downloadUrl(strUrl: String): String?
        {
            var data = ""
            var iStream: InputStream? = null
            var urlConnection: HttpURLConnection? = null
            try {
                val url = URL(strUrl)
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connect()
                iStream = urlConnection!!.inputStream
                val br = BufferedReader(InputStreamReader(iStream))
                val sb = StringBuffer()
                var line: String? = ""
                while (br.readLine().also { line = it } != null)
                {
                   sb.append(line)
                }

                data = sb.toString()
                br.close()
            }
            catch(e: java.lang.Exception) {
                println("Exception: ${e.toString()}")
            }
            finally {
                iStream!!.close()
                urlConnection!!.disconnect()
            }

            return data
        }
    }




    private fun getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_REQUEST_CODE
            )
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        currentLocation = location
                        setLocationOnMap(currentLocation)
                    }
                    println("Latitude: ${location?.latitude} \n Longitude: ${location?.longitude}")
                }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    }

    private fun setLocationOnMap(newLocation: Location)
    {
        val currentLatLng = LatLng(newLocation.latitude, newLocation.longitude)

        // Display address on marker click
        val markerOptions = MarkerOptions().position(currentLatLng)
        val titleStr = getAddress(currentLatLng)
        markerOptions.title(titleStr)

        // mMap.addMarker(markerOptions)
        mMap.addMarker(MarkerOptions().position(currentLatLng).title("Your Location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng))

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // Permission is granted.
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            if (location != null) {
                                currentLocation = location
                                setLocationOnMap(currentLocation)
                            }
                            println("Latitude: ${location?.latitude} \n Longitude: ${location?.longitude}")
                        }
                } else {
                    println("Permission DENIED")
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun getAddress(latLng: LatLng): String {
        // 1
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""

        try {
            // 2
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            // 3
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses[0]
                for (i in 0 until address.maxAddressLineIndex) {
                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(
                        i
                    )
                }
            }
        } catch (e: IOException) {
            Log.e("GoogleMapsActivity", e.localizedMessage)
        }

        println("addressText =" + addressText)
        return addressText
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getLocation()
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }


}