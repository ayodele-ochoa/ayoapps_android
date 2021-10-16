package com.ayodeleochoa.ayoapps.views

import android.Manifest
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ayodeleochoa.ayoapps.DatabaseFragment
import com.ayodeleochoa.ayoapps.R
import kotlin.math.roundToInt


class AccelerometerActivity : AppCompatActivity(), SensorEventListener {
    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null
    private var mAnimatedView: AnimatedView? = null
    val context: Context = this
    var running = false
    var strSteps: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mAnimatedView = AnimatedView(this)
        mAnimatedView!!.setBackgroundColor(getResources().getColor(R.color.high_yella))
        //Set our content to a view, not like the traditional setting to a layout

      //  setContentView(mAnimatedView)
        val knowledgeFragment = DatabaseFragment()
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.knowledge_fragment, knowledgeFragment)
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .commit()
        setContentView(R.layout.activity_accelerometer)
    }

    public fun setAnimatedView()
    {
        setContentView(mAnimatedView)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()

        running = true

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED)
        {
            println("Permission NOT granted")
            requestPermissions(arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 100)
        } else
        {
            println("Permission NOT granted")
            setupStepSensor();
        }
        mSensorManager!!.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        running = false
        mSensorManager!!.unregisterListener(this)
    }

    override fun onAccuracyChanged(arg0: Sensor?, arg1: Int) {}
    override fun onSensorChanged(event: SensorEvent)
    {
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER)
        {
            mAnimatedView!!.onSensorEventSteps(event)
        }
        else if (event.sensor.type == Sensor.TYPE_ACCELEROMETER)
        {
            mAnimatedView!!.onSensorEvent(event)
        }
    }

    fun setupStepSensor()
    {
        var stepsSensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepsSensor == null)
        {
            Toast.makeText(this, "No Step Counter Sensor !", Toast.LENGTH_SHORT).show()
            println("stepsSensor is null...")
        }
        else
        {
            mSensorManager?.registerListener(this, stepsSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out kotlin.String>, grantResults: IntArray): Unit
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            setupStepSensor()
        }
    }

    inner class AnimatedView(context: Context?) : View(context) {
        private val mPaint: Paint
        private val tPaint: Paint
        private var x = 0
        private var y = 0
        private var viewWidth = 0
        private var viewHeight = 0
        protected override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
            super.onSizeChanged(w, h, oldw, oldh)
            viewWidth = w
            viewHeight = h
        }

        fun onSensorEvent(event: SensorEvent)
        {
            x = x - event.values[0].toInt()
            y = y + event.values[1].toInt()
            //Make sure we do not draw outside the bounds of the view.
            //So the max values we can draw to are the bounds + the size of the circle
            if (x <= 0 + Companion.CIRCLE_RADIUS) {
                x = 0 + Companion.CIRCLE_RADIUS
            }
            if (x >= viewWidth - Companion.CIRCLE_RADIUS) {
                x = viewWidth - Companion.CIRCLE_RADIUS
            }
            if (y <= 0 + Companion.CIRCLE_RADIUS) {
                y = 0 + Companion.CIRCLE_RADIUS
            }
            if (y >= viewHeight - Companion.CIRCLE_RADIUS) {
                y = viewHeight - Companion.CIRCLE_RADIUS
            }
        }

        fun onSensorEventSteps(event: SensorEvent)
        {
            if (running)
            {
                strSteps = "STEPS TAKEN: ${event!!.values[0].roundToInt()}"
            }
        }

        protected override fun onDraw(canvas: Canvas)
        {
            canvas.drawCircle(x.toFloat(), y.toFloat(), Companion.CIRCLE_RADIUS.toFloat(), mPaint)
            canvas.drawText(strSteps, 25f, 60f, tPaint);

            //We need to call invalidate each time, so that the view continuously draws
            invalidate()
        }


        init
        {
            mPaint = Paint()
            mPaint.setColor(getResources().getColor(R.color.sky_blue))

            tPaint = Paint()
            tPaint.color = Color.BLACK
            tPaint.textSize = 40f
        }
    }

    companion object
    {
        private const val CIRCLE_RADIUS = 40
    }

}