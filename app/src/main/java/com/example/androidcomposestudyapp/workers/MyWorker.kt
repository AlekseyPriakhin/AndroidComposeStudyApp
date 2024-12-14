package com.example.androidcomposestudyapp.workers

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.androidcomposestudyapp.widget.MyWidget

class MyWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        try {
            Log.i("worker", "do work")
            MyWidget().updateAll(applicationContext)
            val manager = GlanceAppWidgetManager(applicationContext)
            return Result.success()
        } catch (th: Throwable) {
            return Result.failure()
        }
    }
}