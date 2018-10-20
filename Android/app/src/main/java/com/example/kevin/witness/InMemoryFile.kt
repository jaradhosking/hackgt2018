package com.example.kevin.witness

import android.annotation.SuppressLint
import android.net.LocalServerSocket
import android.net.LocalSocket
import android.os.AsyncTask
import android.os.AsyncTask.THREAD_POOL_EXECUTOR
import java.io.Closeable
import java.io.FileDescriptor
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger


@SuppressLint("StaticFieldLeak")
class InMemoryFile : Closeable, AutoCloseable {
    private val socketAddress = "${InMemoryFile::class.java.name}-${hashCode()}"
    private val server = LocalServerSocket(socketAddress)
    private val receiver = LocalSocket(LocalSocket.SOCKET_STREAM)
    private val sender: LocalSocket


    companion object {
        val LOG = Logger.getLogger(InMemoryFile::class.java.name)
    }

    init {
        val bufferSize = 1024 * 512  // 512KiB

        val senderTask = object : AsyncTask<Void, Void, LocalSocket>() {
            override fun doInBackground(vararg params: Void?): LocalSocket {
                return server.accept()
            }
        }.executeOnExecutor(THREAD_POOL_EXECUTOR)
        object : AsyncTask<Void, Void, Void?>() {
            override fun doInBackground(vararg params: Void?): Void? {
                while (true) {
                    try {
                        receiver.connect(server.localSocketAddress)
                        return null
                    } catch (e: IOException) {
                        Thread.sleep(5)
                        LOG.info("trying again.")
                        continue
                    }
                }
            }
        }.executeOnExecutor(THREAD_POOL_EXECUTOR).get(5, TimeUnit.SECONDS)

        sender = senderTask.get(5, TimeUnit.SECONDS)
        sender.sendBufferSize = bufferSize
        receiver.receiveBufferSize = bufferSize
        receiver.soTimeout = 5_000
    }

    fun getFd(): FileDescriptor {
        return receiver.fileDescriptor
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
        receiver.close()
    }
}