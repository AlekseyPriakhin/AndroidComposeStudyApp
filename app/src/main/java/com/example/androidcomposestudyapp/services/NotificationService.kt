package com.example.androidcomposestudyapp.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationService : Service() {

    private val scope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate() {
        super.onCreate()
        val notification = NotificationHelper.createNotification(
            this,
            "service",
            "do work..."
        )
        startForeground(
            101,
            notification
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        doWork()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun doWork() {
        scope.launch {
            delay(100_000)
            withContext(Dispatchers.Main) {
                stopSelf()
            }
        }
    }
}