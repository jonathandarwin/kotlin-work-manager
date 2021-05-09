package com.jonathandarwin.workmanagertest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Created By : Jonathan Darwin on May 09, 2021
 */ 
object NotificationUtil {
    fun showNotification(context: Context, title: String, desc: String, data: Int) {
        val intent = Intent(context, ResultActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(ResultActivity.EXTRA_RESULT, data)
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(context, "1")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(desc)
            .setContentIntent(pendingIntent)
            .build()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("1", "Main Notification", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "This is for main notification of the app"
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        NotificationManagerCompat.from(context)
            .notify(1, builder)
    }
}