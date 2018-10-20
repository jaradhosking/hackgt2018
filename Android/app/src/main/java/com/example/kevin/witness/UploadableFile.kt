package com.example.kevin.witness

import android.annotation.SuppressLint
import android.content.Context
import java.io.Closeable
import java.io.File
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.logging.Logger


class UploadableFile(context: Context) : Closeable, AutoCloseable {
    val file = createTempFile("uploadable-file", null, context.externalCacheDir)


    init {
        LOG.info("writing temp file to " + file.absolutePath)
    }
    companion object {
        val LOG = Logger.getLogger(UploadableFile::class.java.name)
    }

    fun putData(): BlockingQueue<WorkerEvent> {
        val eventQueue = ArrayBlockingQueue<WorkerEvent>(100)
        Thread {
            val webb = Keys.blobRequestor
            val dataId = webb.put("/jcl")
                    .header("x-ms-blob-content-type", "audio/webm")
                    .header("x-ms-blob-type", "AppendBlob")
                    .ensureSuccess()
                    .asJsonObject()
            LOG.info(dataId.body.toString())
        }.start()
        return eventQueue
    }

    override fun close() {
        file.delete()
    }
}