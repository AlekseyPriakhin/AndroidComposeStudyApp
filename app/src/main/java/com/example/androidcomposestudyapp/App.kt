package com.example.androidcomposestudyapp

import android.app.Application
import android.app.NotificationManager
import androidx.core.content.getSystemService
import com.example.androidcomposestudyapp.di.appModule
import com.example.androidcomposestudyapp.services.NotificationHelper
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        getSystemService<NotificationManager>()?.let {
            NotificationHelper.createChannels(it)
        }
        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(appModule)
        }
    }

}