package com.ayodeleochoa.ayoapps.views

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ayodeleochoa.ayoapps.DatabaseFragment
import com.ayodeleochoa.ayoapps.R
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class AdmobActivity : AppCompatActivity()
{
    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "AdmobActivity"
    var clickCount = 5
    lateinit var AdButton: Button

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admob)

        // Show knowledge fragment
        val knowledgeFragment = DatabaseFragment()
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.knowledge_fragment, knowledgeFragment)
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .commit()

        MobileAds.initialize(this) {}
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        var adRequestFull = AdRequest.Builder().build()

        AdButton = findViewById<Button>(R.id.btnAds)
        AdButton.setOnClickListener {
            clickCount--

            if (clickCount == 0)
            {
                println("clickCount = $clickCount")
                if (mInterstitialAd != null)
                {
                    mInterstitialAd?.show(this)

                    InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequestFull, object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.d(TAG, adError?.message)
                            mInterstitialAd = null
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            Log.d(TAG, "Ad was loaded.")
                            mInterstitialAd = interstitialAd
                        }
                    })
                }
                else
                {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
                }
                clickCount = 5
            }

            AdButton.setText("CLICK $clickCount TIMES FOR FULL SCREEN AD")
        }


        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError?.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })

        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad was dismissed.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d(TAG, "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen content.")
                mInterstitialAd = null
            }
        }
    }
}