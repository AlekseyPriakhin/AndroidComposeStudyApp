package com.example.androidcomposestudyapp.media
import android.app.Notification
import android.app.Notification.MediaStyle
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.androidcomposestudyapp.R
import com.example.androidcomposestudyapp.services.NotificationHelper.DEFAULT_CHANNEL_ID

class MediaService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying = false

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.audio)
        mediaPlayer.isLooping = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            STARTFOREGROUND_ACTION -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    startForeground(
                        NOTIFICATION_ID,
                        createNotification(),
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                    )
                } else {
                    startForeground(NOTIFICATION_ID, createNotification())
                }
                if (!mediaPlayer.isPlaying) {
                    mediaPlayer.start()
                }
                isPlaying = true
            }

            STOPFOREGROUND_ACTION -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                    mediaPlayer.seekTo(0)
                }
                isPlaying = false
                stopSelf()
            }

            else -> {
                // do nothing
            }
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotification(): Notification {
        val stopIntent = Intent(this, MediaService::class.java)
        stopIntent.action = STOPFOREGROUND_ACTION
        val pendingStopIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
            .setContentTitle("Music Player")
            .setContentText("Playing Music")
            .setSmallIcon(R.drawable.like)
            .addAction(R.drawable.arrow_back, "Stop", pendingStopIntent)

        return builder.build()
    }

    companion object {
        const val STARTFOREGROUND_ACTION = "com.example.musicplayer.action.START_FOREGROUND"
        const val STOPFOREGROUND_ACTION = "com.example.musicplayer.action.STOP_FOREGROUND"

        const val NOTIFICATION_ID = 1
    }
}