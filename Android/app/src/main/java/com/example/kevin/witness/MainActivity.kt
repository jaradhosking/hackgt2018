package com.example.kevin.witness

import android.Manifest.permission.RECORD_AUDIO
import android.media.MediaRecorder
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.wearable.activity.WearableActivity
import android.widget.TextView
import java.util.logging.Logger
import android.content.Intent
import com.danielkim.soundrecorder.RecordingService


class MainActivity : WearableActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    companion object {
        val LOG = Logger.getLogger(MainActivity::class.java.name)
    }

    private var textView: TextView? = null
    private var micRecorder = MediaRecorder();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.text)
        this.requestPermissions(arrayOf(RECORD_AUDIO), 0)

        // Enables Always-on
        setAmbientEnabled()
    }

    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 0) {
            LOG.info("Received audio permission!")
            this.startService(Intent(this, RecordingService::class.java))
        }
    }
}
