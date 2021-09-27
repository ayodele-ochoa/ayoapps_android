package com.ayodeleochoa.ayoapps.views

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ayodeleochoa.ayoapps.DatabaseFragment
import com.ayodeleochoa.ayoapps.R







class BluetoothActivity : AppCompatActivity()
{
    lateinit var btAdapter: BluetoothAdapter
    val PERMISSION_BLUETOOTH_ADAPTER = 1
    val PERMISSION_BLUETOOTH_DISCOVERABLE = 2

    // val bluetoothDevices = ArrayList<BluetoothDevice>()
    val bluetoothDeviceList = ArrayList<String>()
    private lateinit var devicesListView: ListView
    val pairedList = ArrayList<String>()
    private lateinit var pairedListView: ListView

    val contextBT: Context = this

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        // Show knowledge fragment
        val knowledgeFragment = DatabaseFragment()
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.knowledge_fragment, knowledgeFragment)
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .commit()

        val buttonBTon = findViewById<Button>(R.id.btnBluetoothOn)
        val buttonBToff = findViewById<Button>(R.id.btnBluetoothOff)
        val buttonDiscoverable = findViewById<Button>(R.id.btnDiscoverable)
        val buttonFindDevices = findViewById<Button>(R.id.btnFindDevices)
        val buttonPairedDevices = findViewById<Button>(R.id.btnFindPairedDevices)
        val textBT = findViewById<TextView>(R.id.txtBluetooth)

        devicesListView = findViewById<ListView>(R.id.devices_list_view)
        pairedListView = findViewById<ListView>(R.id.paired_list_view)



        val devicesHeaderTextview = TextView(contextBT)
        devicesHeaderTextview.gravity = Gravity.CENTER
        devicesHeaderTextview.setBackgroundColor(Color.LTGRAY)
        devicesHeaderTextview.setPadding(0, 15, 0, 15)
        devicesHeaderTextview.text = "DEVICES FOUND"
        devicesListView.addHeaderView(devicesHeaderTextview);

        val pairedHeaderTextview = TextView(contextBT)
        pairedHeaderTextview.gravity = Gravity.CENTER
        pairedHeaderTextview.setBackgroundColor(Color.LTGRAY)
        pairedHeaderTextview.setPadding(0, 15, 0, 15)
        pairedHeaderTextview.text = "PAIRED DEVICES"
        pairedListView.addHeaderView(pairedHeaderTextview);




        checkBluetoothPermissions()

        // val getVisible = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        // startActivityForResult(getVisible, 0)

        btAdapter = BluetoothAdapter.getDefaultAdapter()
        if (btAdapter.isEnabled)
        {
            println("Bluetooth is ON")
            textBT.text = "ON"
            textBT.setTextColor(Color.GREEN)

        }
        else
        {
            println("Bluetooth is OFF")
            textBT.text = "OFF"
            textBT.setTextColor(Color.RED)
        }

        buttonBTon.setOnClickListener {
            if (!btAdapter.isEnabled())
            {
            //    val turnOn = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
             //   startActivityForResult(turnOn, 0)
             //   startForResult.launch(turnOn)
                 btAdapter.enable()
                Toast.makeText(applicationContext, "Bluetooth turned ON!", Toast.LENGTH_LONG).show()
                textBT.text = "ON"
                textBT.setTextColor(Color.GREEN)

            }
            else
            {
                Toast.makeText(applicationContext, "Bluetooth is already ON.", Toast.LENGTH_LONG).show()
            }

        }

        buttonBToff.setOnClickListener {
            if (!btAdapter.isEnabled() == false)
            {
                btAdapter.disable()
                Toast.makeText(applicationContext, "Bluetooth turned OFF.", Toast.LENGTH_LONG).show()
                textBT.text = "OFF"
                textBT.setTextColor(Color.RED)
            }
            else
            {
                Toast.makeText(applicationContext, "Bluetooth is already OFF.", Toast.LENGTH_LONG).show()
            }

        }

        buttonDiscoverable.setOnClickListener {
            val requestCode = 1;
            val discoverableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
            }
            startActivityForResult(discoverableIntent, requestCode)
        }

        buttonFindDevices.setOnClickListener {
            findBluetoothDevices()
        }

        buttonPairedDevices.setOnClickListener {
            findPairedDevices()
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode)
        {
            PERMISSION_BLUETOOTH_ADAPTER -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // Permission is granted.
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.BLUETOOTH
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.BLUETOOTH_ADMIN
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    // Put in code to turn bluetooth on.
                    println("Bluetooth permission granted")
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

    private fun checkBluetoothPermissions()
    {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.BLUETOOTH_ADMIN
            ) != PackageManager.PERMISSION_GRANTED
        )
        {
            println("Permission NOT granted")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.BLUETOOTH,
                    android.Manifest.permission.BLUETOOTH_ADMIN
                ),
                PERMISSION_BLUETOOTH_ADAPTER
            )
        }
        else
        {
            println("Permission already granted")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkBTPermissions()
    {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
        {
            var permissionCheck = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            permissionCheck += checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            if (permissionCheck != 0)
            {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    PERMISSION_BLUETOOTH_ADAPTER)
            }
            else
            {
                "Permissions already granted..."
            }
        }
    }


    private fun findPairedDevices()
    {
        val pairedDevices: Set<BluetoothDevice>? = btAdapter?.bondedDevices
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address // MAC address

            if (device != null)
            {
                pairedList.add("Device Name: ${deviceName}  Mac Address: ${deviceHardwareAddress}")
            }

        }

        println("pairedList.count() = " + pairedList.count())
        if (pairedList.count() > 0)
        {
            val adapter = ArrayAdapter(contextBT, android.R.layout.simple_list_item_1, pairedList)
            pairedListView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        else
        {
            Toast.makeText(applicationContext, "No paired devices found.", Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun findBluetoothDevices()
    {
        println("Find Bluetooth devices")
        checkBluetoothPermissions()
        bluetoothDeviceList.clear()
        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)
        btAdapter.startDiscovery()

    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {

        @RequiresApi(Build.VERSION_CODES.R)
        override fun onReceive(context: Context, intent: Intent)
        {

            val action: String? = intent.action
            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    var deviceName = device?.name
                    var deviceHardwareAddress = device?.address // MAC address
                    val deviceUUID = device?.uuids
                    val deviceAlias = device?.alias
                    val bluetoothClass = device?.bluetoothClass
                    println("deviceName = " + deviceName)
                    println("deviceHardwareAddress = " + deviceHardwareAddress)

                    if (deviceName == null)
                    {
                        deviceName = "Unknown"
                    }
                    if (deviceHardwareAddress == null)
                    {
                        deviceHardwareAddress = "Unknown"
                    }



                    if (device != null)
                    {
                        bluetoothDeviceList.add("Device Name: ${deviceName}  Mac Address: ${deviceHardwareAddress}")
                      //  bluetoothDevices.add(device)
                    }
                }
            }

            val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, bluetoothDeviceList)
            devicesListView.adapter = adapter
            adapter.notifyDataSetChanged()

            println("bluetoothDevices count = " + bluetoothDeviceList.count())

        }


    }




}