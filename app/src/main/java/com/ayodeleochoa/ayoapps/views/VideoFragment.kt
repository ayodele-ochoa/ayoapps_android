package com.ayodeleochoa.ayoapps.views

import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.ayodeleochoa.ayoapps.R
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VideoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var videoView: VideoView
    lateinit var progressBar: ProgressBar
    lateinit var textVideoTime: TextView
    lateinit var buttonPausePlay: ImageButton
    var progressBarStatus = 0
    var newPosition: Int = 0
    var duration: Int = 0
    var textDuration: String = ""
    lateinit var timer: Timer
    lateinit var timeThread: Thread
    var timerRunning: Boolean = false

    val videosList: ArrayList<Uri> = ArrayList(3)
    var videoPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(com.ayodeleochoa.ayoapps.R.layout.fragment_video, container, false)

        videoView = view.findViewById<VideoView>(com.ayodeleochoa.ayoapps.R.id.videoView)
        progressBar = view.findViewById<ProgressBar>(com.ayodeleochoa.ayoapps.R.id.pbVideo)
        textVideoTime = view.findViewById<TextView>(com.ayodeleochoa.ayoapps.R.id.txtVideoTime)

        //Creating MediaController
        val mediaController = MediaController(context)
        mediaController.setAnchorView(videoView)
        //specify the location of media file
      //  val uri: Uri = parse(Environment.getExternalStorageDirectory().getPath() + "/raw/swirly_video.mp4")
            //  println("movie uri = " + uri)

        val beautyBeast = Uri.parse("android.resource://${activity?.packageName}/${R.raw.beauty_beast}")
        videosList.add(beautyBeast)


        //Setting MediaController and URI, then starting the videoView
        videoView.setMediaController(null)
        videoView.setVideoURI(beautyBeast)
        videoView.requestFocus()
        videoView.start()

        videoView.setOnPreparedListener(OnPreparedListener
        {
            duration = videoView.getDuration()
         //   println("duration = " + duration)
            progressBar.max = duration
           // runProgressBar()

            timer = Timer()
            val task: TimerTask = object : TimerTask() {
                override fun run() {
                    progressBar.progress = videoView.currentPosition
                    updateUI()
                }
            }
            timer.schedule(task, 0, 100)
            setDurationUI()

        })


        buttonPausePlay = view.findViewById<ImageButton>(com.ayodeleochoa.ayoapps.R.id.btnPausePlay)
        buttonPausePlay.setOnClickListener {
       //   println("isPlaying = " + videoView.isPlaying)
            if (videoView.isPlaying)
            {

                videoView.pause()
                newPosition = videoView.getCurrentPosition() + 500;
                buttonPausePlay.setImageResource(R.drawable.play50)
            }
            else
            {
                println("Resume")
                videoView.seekTo(newPosition)
                videoView.start()
                buttonPausePlay.setImageResource(R.drawable.pause50)
            }

        }

        val buttonFastFoward = view.findViewById<ImageButton>(com.ayodeleochoa.ayoapps.R.id.btnForward)
        buttonFastFoward.setOnClickListener {
            videoView.pause()
            newPosition = videoView.getCurrentPosition() + 10000;
            videoView.seekTo(newPosition)
            videoView.start()
            buttonPausePlay.setImageResource(R.drawable.pause50)
        }

        val buttonRewind = view.findViewById<ImageButton>(com.ayodeleochoa.ayoapps.R.id.btnRewind)
        buttonRewind.setOnClickListener {
            videoView.pause()
            newPosition = videoView.getCurrentPosition() - 10000;
            videoView.seekTo(newPosition)
            videoView.start()
            buttonPausePlay.setImageResource(R.drawable.pause50)
        }

        /*val buttonSkipNext = view.findViewById<ImageButton>(com.ayodeleochoa.ayoapps.R.id.btnSkipNext)
        buttonSkipNext.setOnClickListener {
            nextVideo()
        }

        val buttonSkipPrevious = view.findViewById<ImageButton>(com.ayodeleochoa.ayoapps.R.id.btnSkipPrevious)
        buttonSkipPrevious.setOnClickListener {
            previousVideo()
        }*/

        // Get the audio manager system service
      //  val audioManager: AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val audioManager = activity?.applicationContext?.getSystemService(AUDIO_SERVICE) as AudioManager

        val buttonVolumeUp = view.findViewById<ImageButton>(com.ayodeleochoa.ayoapps.R.id.btnVolumeUpVideo)
        buttonVolumeUp.setOnClickListener {
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND)
        }

        val buttonVolumeDown = view.findViewById<ImageButton>(com.ayodeleochoa.ayoapps.R.id.btnVolumeDownVideo)
        buttonVolumeDown.setOnClickListener {
            audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND)
        }

        return view
    }

    private fun runProgressBar()
    {
        timerRunning = true
        // task is run on a thread
       Thread(Runnable {


                while (videoView.currentPosition < duration && videoView != null)
                {
                    // performing some dummy operation
                    try {
                        Thread.sleep(500)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                    // Updating the progress bar
                    //   println("videoView.currentPosition =" + videoView.currentPosition)
                    if (videoView != null)
                    {
                        progressBar.progress = videoView.currentPosition
                        updateUI()
                    }

                }



        }).start()



    }

    private fun timerCounter() {
        timer = Timer()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                 runProgressBar()
            }
        }
        timer.schedule(task, 0, 100)
    }

    private fun setDurationUI()
    {
        val durationSeconds  = duration / 1000
        val hours = durationSeconds / 3600
        val minutes = durationSeconds / 60 - hours * 60
        val seconds = durationSeconds - hours * 3600 - minutes * 60
        textDuration = String.format("%02d:%02d", minutes, seconds)
      //  textVideoTime.text = textDuration
    }

    private fun updateUI()
    {
        val currentSeconds  = videoView.currentPosition / 1000
        val hours = currentSeconds / 3600
        val minutes = currentSeconds / 60 - hours * 60
        val seconds = currentSeconds - hours * 3600 - minutes * 60
        var textCurrentSeconds = String.format("%02d:%02d / $textDuration", minutes, seconds)
        textVideoTime.text = textCurrentSeconds
    }

    public fun pauseVideo()
    {
        if (videoView.isPlaying)
        {

            videoView.pause()
            newPosition = videoView.getCurrentPosition() + 500;
            buttonPausePlay.setImageResource(R.drawable.play50)
        }
    }

   /* private fun nextVideo()
    {
        videoPosition++

        if (videoPosition == videosList.count())
        {
            videoPosition = 0
        }

        videoView.setVideoURI(videosList.get(videoPosition))
        videoView.requestFocus()
        videoView.start()
    }

    private fun previousVideo()
    {
        videoPosition--

        if (videoPosition < 0)
        {
            videoPosition = videosList.count() - 1
        }

        videoView.setVideoURI(videosList.get(videoPosition))
        videoView.requestFocus()
        videoView.start()
    }*/

    override fun onPause()
    {
        super.onPause()
        timer.cancel()
        "VideoFragment Paused"
    }

    override fun onResume() {
        super.onResume()
        "VideoFragment Resumed"
    }

    override fun onDestroy() {
        super.onDestroy()
        "VideoFragment Destroyed"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        "VideoFragment onSave: $outState"
    }

    override fun onStop() {
        super.onStop()
        "VideoFragment Stopped"
    }

    override fun onStart() {
        super.onStart()
        "VideoFragment Started"
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VideoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VideoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}