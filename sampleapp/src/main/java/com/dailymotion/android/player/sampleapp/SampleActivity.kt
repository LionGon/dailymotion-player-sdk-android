package com.dailymotion.android.player.sampleapp

import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.dailymotion.android.player.sdk.events.*
import com.dailymotion.websdksample.R
import kotlinx.android.synthetic.main.new_screen_sample.*
import java.util.*

class SampleActivity : AppCompatActivity(), View.OnClickListener {

    private var mFullscreen = false

    private fun onFullScreenToggleRequested() {
        setFullScreenInternal(!mFullscreen)
        val params: LinearLayout.LayoutParams

        if (mFullscreen) {
            toolbar?.visibility = View.GONE
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        } else {
            toolbar?.visibility = View.VISIBLE
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (215 * resources.displayMetrics.density).toInt())
        }
        dm_player_web_view.layoutParams = params
    }

    private fun setFullScreenInternal(fullScreen: Boolean) {
        mFullscreen = fullScreen
        if (mFullscreen) {
            action_layout.visibility = View.GONE
        } else {
            action_layout.visibility = View.VISIBLE
        }
        dm_player_web_view.setFullscreenButton(mFullscreen)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.new_screen_sample)

        setSupportActionBar(toolbar)

        @Suppress("DEPRECATION")
        toolbar?.let {
            it.visibility = View.VISIBLE
            it.setBackgroundColor(resources.getColor(android.R.color.background_dark))
            it.setTitleTextColor(resources.getColor(android.R.color.white))

            val actionBar = supportActionBar
            actionBar?.title = getString(R.string.app_name)
        }

        if (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0) {
            dm_player_web_view.setIsWebContentsDebuggingEnabled(true)
        }

        val playerParams = HashMap<String, String>()
        dm_player_web_view.load("x26hv6c", playerParams as Map<String, Any>?)

        dm_player_web_view.setPlayerEventListener { playerEvent ->
            when (playerEvent) {
                is ApiReadyEvent -> log(playerEvent.name)
                is StartEvent -> log(playerEvent.name)
                is LoadedMetaDataEvent -> log(playerEvent.name)
                is ProgressEvent -> log(playerEvent.name + " (bufferedTime: " + dm_player_web_view.bufferedTime + ")")
                is DurationChangeEvent -> log(playerEvent.name + " (duration: " + dm_player_web_view.duration + ")")
                is TimeUpdateEvent, is AdTimeUpdateEvent, is SeekingEvent, is SeekedEvent -> log(playerEvent.name + " (currentTime: " + dm_player_web_view.position + ")")
                is VideoStartEvent, is AdStartEvent, is AdPlayEvent, is PlayingEvent, is EndEvent -> log(playerEvent.name + " (ended: " + dm_player_web_view.isEnded + ")")
                is AdPauseEvent, is AdEndEvent, is VideoEndEvent, is PlayEvent, is PauseEvent -> log(playerEvent.name + " (paused: " + dm_player_web_view.videoPaused + ")")
                is QualityChangeEvent -> log(playerEvent.name + " (quality: " + dm_player_web_view.quality + ")")
                is VolumeChangeEvent -> log(playerEvent.name + " (volume: " + dm_player_web_view.volume + ")")
                is FullScreenToggleRequestedEvent -> onFullScreenToggleRequested()
            }
        }

        btnTogglePlay.setOnClickListener(this@SampleActivity)
        btnPlay.setOnClickListener(this@SampleActivity)
        btnPause.setOnClickListener(this@SampleActivity)
        btnSeek.setOnClickListener(this@SampleActivity)
        btnLoadVideo.setOnClickListener(this@SampleActivity)
        btnSetQuality.setOnClickListener(this@SampleActivity)
        btnSetSubtitle.setOnClickListener(this@SampleActivity)
        btnToggleControls.setOnClickListener(this@SampleActivity)
        btnShowControls.setOnClickListener(this@SampleActivity)
        btnHideControls.setOnClickListener(this@SampleActivity)
        btnSetVolume.setOnClickListener(this@SampleActivity)

        dm_player_web_view.playWhenReady = false
    }

    override fun onClick(v: View) {
        when(v) {
            btnPlay -> dm_player_web_view.play()
            btnTogglePlay -> dm_player_web_view.togglePlay()
            btnPause -> dm_player_web_view.pause()
            btnSeek -> dm_player_web_view.seek(30.0)
            btnLoadVideo -> dm_player_web_view.load("x19b6ui")
            btnSetQuality -> dm_player_web_view.quality = "240"
            btnSetSubtitle -> dm_player_web_view.setSubtitle("en")
            btnToggleControls -> dm_player_web_view.toggleControls()
            btnShowControls -> dm_player_web_view.showControls(true)
            btnHideControls -> dm_player_web_view.showControls(false)
            btnSetVolume -> {
                val text = (findViewById<View>(R.id.editTextVolume) as EditText).text.toString()
                val volume = java.lang.Float.parseFloat(text)
                dm_player_web_view.volume = volume
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.sample, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    override fun onBackPressed() {
        dm_player_web_view.goBack()
    }

    override fun onPause() {
        super.onPause()
        dm_player_web_view.onPause()
    }

    override fun onResume() {
        super.onResume()
        dm_player_web_view.onResume()
    }

    private fun log(text: String) {
        if (action_layout.visibility == View.GONE) {
            return
        }

        logText.append("\n" + text)
        val scroll = logText.layout.getLineTop(logText.lineCount) - logText.height
        if (scroll > 0) {
            logText!!.scrollTo(0, scroll)
        } else {
            logText!!.scrollTo(0, 0)
        }
    }
}
