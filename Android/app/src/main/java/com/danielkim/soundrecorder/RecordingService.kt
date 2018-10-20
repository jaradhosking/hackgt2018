package com.danielkim.soundrecorder

import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.IBinder
import android.widget.Toast
import com.example.kevin.witness.UploadableFile
import java.util.logging.Logger

/**
 * Created by Daniel on 12/28/2014.
 */
class RecordingService : Service() {

    private var destFile: UploadableFile = UploadableFile(applicationContext)
    private var recorder = MediaRecorder()

    companion object {
        val LOG = Logger.getLogger(RecordingService::class.java.name)!!
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startRecording()
        return Service.START_STICKY
    }

    override fun onDestroy() {
        stopRecording()
        destFile.close()
        super.onDestroy()
    }

    fun startRecording() {
        recorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(destFile.file.absolutePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioChannels(1)
            setAudioSamplingRate(44100)
            setAudioEncodingBitRate(28_000)
            prepare()
            start()
        }
        LOG.info("Started recording")
    }

    fun stopRecording() {
        LOG.info("Stopping recording...")
        recorder.stop()
        recorder.release()
        recorder.reset()
        Toast.makeText(this,
                "Recording done: " + destFile.file.absolutePath,
                Toast.LENGTH_LONG).show()
        LOG.info("...Stopped recording")
    }
}
