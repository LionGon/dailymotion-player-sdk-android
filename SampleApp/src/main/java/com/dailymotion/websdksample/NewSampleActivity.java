package com.dailymotion.websdksample;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dailymotion.websdk.DMPlayerWebView;

import java.util.HashMap;

import timber.log.Timber;

public class NewSampleActivity extends Activity implements View.OnClickListener, FullScreenListener {

    private DMPlayerWebView mVideoView;
    private TextView mLogText;
    private boolean mFullscreen = false;

    @Override
    public void onFullScreen(boolean fullscreen) {
        setFullScreenInternal(!mFullscreen);

        if (mFullscreen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }

    private void setFullScreenInternal(boolean fullScreen) {
        mFullscreen = fullScreen;
        mVideoView.setFullscreenButton(mFullscreen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.plant(new Timber.DebugTree());
        setContentView(R.layout.new_screen_sample);

        mVideoView = (DMPlayerWebView) findViewById(R.id.dm_player_web_view);
        mVideoView.playVideo("x26hv6c");

        mVideoView.setEventListener(new DMPlayerWebView.EventListener() {
            @Override
            public void onEvent(String event, HashMap<String, String> map) {
                switch (event) {
                    case "apiready":
                        log("apiready");
                        break;
                    case "start":
                        log("start");
                        break;
                    case "loadedmetadata":
                        log("loadedmetadata");
                        break;
                    case "progress":
                        log(event + " (bufferedTime: " + mVideoView.getBufferedTime() + ")");
                        break;
                    case "durationchange":
                        log(event + " (duration: " + mVideoView.getDuration() + ")");
                        break;
                    case "timeupdate":
                    case "ad_timeupdate":
                    case "seeking":
                    case "seeked":
                        log(event + " (currentTime: " + mVideoView.getPosition() + ")");
                        break;
                    case "video_start":
                    case "ad_start":
                    case "ad_play":
                    case "playing":
                    case "end":
                        log(event + " (ended: " + mVideoView.isEnded() + ")");
                        break;
                    case "ad_pause":
                    case "ad_end":
                    case "video_end":
                    case "play":
                    case "pause":
                        log(event + " (paused: " + mVideoView.getVideoPaused() + ")");
                        break;
                    case "qualitychange":
                        log(event + " (quality: " + mVideoView.getQuality() + ")");
                        break;
                    default:
                        break;
                }
            }
        });

        Button playButton = ((Button) findViewById(R.id.btnTogglePlay));
        playButton.setOnClickListener(NewSampleActivity.this);
        Button togglePlayButton = ((Button) findViewById(R.id.btnPlay));
        togglePlayButton.setOnClickListener(NewSampleActivity.this);
        Button pauseButton = ((Button) findViewById(R.id.btnPause));
        pauseButton.setOnClickListener(NewSampleActivity.this);

        Button seekButton = ((Button) findViewById(R.id.btnSeek));
        seekButton.setOnClickListener(NewSampleActivity.this);
        Button loadVideoButton = ((Button) findViewById(R.id.btnLoadVideo));
        loadVideoButton.setOnClickListener(NewSampleActivity.this);
        Button setQualityButton = ((Button) findViewById(R.id.btnSetQuality));
        setQualityButton.setOnClickListener(NewSampleActivity.this);
        Button setSubtitleButton = ((Button) findViewById(R.id.btnSetSubtitle));
        setSubtitleButton.setOnClickListener(NewSampleActivity.this);

        Button toggleControlsButton = ((Button) findViewById(R.id.btnToggleControls));
        toggleControlsButton.setOnClickListener(NewSampleActivity.this);
        Button showControlsButton = ((Button) findViewById(R.id.btnShowControls));
        showControlsButton.setOnClickListener(NewSampleActivity.this);
        Button hideControlsButton = ((Button) findViewById(R.id.btnHideControls));
        hideControlsButton.setOnClickListener(NewSampleActivity.this);

        mLogText = ((TextView) findViewById(R.id.logText));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPlay) {
            mVideoView.play();
        } else if (v.getId() == R.id.btnTogglePlay) {
            mVideoView.togglePlay();
        } else if (v.getId() == R.id.btnPause) {
            mVideoView.pause();
        } else if (v.getId() == R.id.btnSeek) {
            mVideoView.seek(30);
        } else if (v.getId() == R.id.btnLoadVideo) {
            mVideoView.playVideo("x19b6ui");
        } else if (v.getId() == R.id.btnSetQuality) {
            mVideoView.setQuality("240");
        } else if (v.getId() == R.id.btnSetSubtitle) {
            mVideoView.setSubtitle("en");
        } else if (v.getId() == R.id.btnToggleControls) {
            mVideoView.toggleControls();
        } else if (v.getId() == R.id.btnShowControls) {
            mVideoView.showControls(true);
        } else if (v.getId() == R.id.btnHideControls) {
            mVideoView.showControls(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sample, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        mVideoView.goBack();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mVideoView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mVideoView.onResume();
        }
    }

    private void log(String text) {
        mLogText.append("\n" + text);
        final int scroll = mLogText.getLayout().getLineTop(mLogText.getLineCount()) - mLogText.getHeight();
        if (scroll > 0) {
            mLogText.scrollTo(0, scroll);
        } else {
            mLogText.scrollTo(0, 0);
        }
    }
}
