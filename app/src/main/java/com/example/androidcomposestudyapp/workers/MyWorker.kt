package com.example.androidcomposestudyapp.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class MyWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        try {
            Log.i("worker", "do work")
            return Result.success()
        } catch (th: Throwable) {
            return Result.failure()
        }
    }
}