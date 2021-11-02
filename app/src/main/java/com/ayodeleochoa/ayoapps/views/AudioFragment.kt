package com.ayodeleochoa.ayoapps.views

import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.ayodeleochoa.ayoapps.R
import com.bumptech.glide.Glide
import java.util.*




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AudioFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AudioFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var audioImage: ImageView
    lateinit var buttonPausePlayAudio: ImageButton
    lateinit var buttonRecordAudio: ImageButton
    lateinit var mediaPlayer: MediaPlayer
    var newPosition: Int = 0
    lateinit var progressBar: ProgressBar
    var duration: Int = 0
    lateinit var timer: Timer
    var textDuration: String = ""
    lateinit var textAudioTime: TextView

    var permissionToRecordAccepted = false


    var recorder: MediaRecorder? = null

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

        val view = inflater.inflate(com.ayodeleochoa.ayoapps.R.layout.fragment_audio, container, false)

        audioImage = view.findViewById(com.ayodeleochoa.ayoapps.R.id.imgAudio)
        var audioGif = R.drawable.audio_gif
        var audioPNG = R.drawable.audio_png

        mediaPlayer = MediaPlayer.create(context, R.raw.aladdin)
        progressBar = view.findViewById<ProgressBar>(com.ayodeleochoa.ayoapps.R.id.pbAudio)

        duration = mediaPlayer.duration
      //  println("duration = " + duration)
        progressBar.max = duration
        textAudioTime = view.findViewById<TextView>(com.ayodeleochoa.ayoapps.R.id.txtAudioTime)
        // runProgressBar()

        timer = Timer()
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                progressBar.progress = mediaPlayer.currentPosition
                updateUI()
            }
        }
        timer.schedule(task, 0, 500)
        setDurationUI()

        buttonPausePlayAudio = view.findViewById(com.ayodeleochoa.ayoapps.R.id.btnPausePlayAudio)
        buttonPausePlayAudio.setOnClickListener {

            if (mediaPlayer.isPlaying)
            {
                mediaPlayer.pause()
                Glide.with(requireContext())
                    .load(audioPNG)
                    .into(audioImage)
                buttonPausePlayAudio.setImageResource(R.drawable.play50)
            }
            else
            {
                mediaPlayer.start()
                Glide.with(requireContext())
                    .load(audioGif)
                    .into(audioImage)
                buttonPausePlayAudio.setImageResource(R.drawable.pause50)
            }


        }

        mediaPlayer.setOnCompletionListener(OnCompletionListener {
            Glide.with(requireContext())
                .load(audioPNG)
                .into(audioImage)
            buttonPausePlayAudio.setImageResource(R.drawable.play50)
        })

        val buttonFastForwardAudio = view.findViewById<ImageButton>(com.ayodeleochoa.ayoapps.R.id.btnForwardAudio)
        buttonFastForwardAudio.setOnClickListener {
            println("buttonFastForwardAudio pressed")
            mediaPlayer.pause()
            newPosition = mediaPlayer.getCurrentPosition() + 10000;
            mediaPlayer.seekTo(newPosition)
            mediaPlayer.start()
            buttonPausePlayAudio.setImageResource(R.drawable.pause50)
        }

        val buttonRewindAudio = view.findViewById<ImageButton>(com.ayodeleochoa.ayoapps.R.id.btnRewindAudio)
        buttonRewindAudio.setOnClickListener {
            mediaPlayer.pause()
            newPosition = mediaPlayer.getCurrentPosition() - 10000;
            mediaPlayer.seekTo(newPosition)
            mediaPlayer.start()
            buttonPausePlayAudio.setImageResource(R.drawable.pause50)
        }


        val audioManager = activity?.applicationContext?.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val buttonVolumeUp = view.findViewById<ImageButton>(com.ayodeleochoa.ayoapps.R.id.btnVolumeUpAudio)
        buttonVolumeUp.setOnClickListener {
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND)
        }

        val buttonVolumeDown = view.findViewById<ImageButton>(com.ayodeleochoa.ayoapps.R.id.btnVolumeDownAudio)
        buttonVolumeDown.setOnClickListener {
            audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND)
        }



        return view
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out kotlin.String>, grantResults: IntArray): Unit {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
          //  startRecording()
        }
    }

    private fun updateUI()
    {
        val currentSeconds  = mediaPlayer.currentPosition / 1000
        val hours = currentSeconds / 3600
        val minutes = currentSeconds / 60 - hours * 60
        val seconds = currentSeconds - hours * 3600 - minutes * 60
        var textCurrentSeconds = String.format("%02d:%02d / $textDuration", minutes, seconds)
        textAudioTime.text = textCurrentSeconds
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

    /*@RequiresApi(Build.VERSION_CODES.S)
    private fun startRecording()
    {
        println("startRecording")
        // initialize and configure MediaRecorder

        val output = Environment.getExternalStorageDirectory().absolutePath + File.separator + "recording.3gp"
        println("Destination output: $output")
      //  val output2 = "${externalCacheDir.absolutePath}/audiorecordtest.3gp"

        var FileName = "recording.m4a"
        val destDir = File(Environment.getExternalStorageDirectory().toString() + "/test/")
        println()
        if (!destDir.exists())
        {
            destDir.mkdirs()
            println("Destination did not exist")
        }
        val FilePath = Environment.getExternalStorageDirectory().toString() + "/test/" + FileName
        recorder = MediaRecorder()
        recorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder!!.setOutputFile(output)
        recorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        try {
            recorder!!.prepare()
        } catch (e: IOException) {
            // handle error
        } catch (e: IllegalStateException) {
            // handle error
        }


        recorder!!.start()
    }*/





    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AudioFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AudioFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}