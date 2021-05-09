package com.jonathandarwin.workmanagertest.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.jonathandarwin.workmanagertest.MainActivity
import com.jonathandarwin.workmanagertest.R
import com.jonathandarwin.workmanagertest.ResultActivity

/**
 * Created By : Jonathan Darwin on May 09, 2021
 */ 
object NotificationUtil {

    /**
     * Assume that this showNotification() is dedicated for ResultActivity only
     * because the code inside is written for navigate to ResultActivity
     */
    fun showNotification(context: Context, title: String, desc: String, data: Int) {
        val intentToMain = Intent(context, MainActivity::class.java)
        val intentToResult = Intent(context, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_RESULT, data)
        }

        // Create Task Stack & Pending Intent
        val pendingIntent = TaskStackBuilder.create(context)
                        .addParentStack(MainActivity::class.java)
                        .addNextIntent(intentToMain)
                        .addNextIntent(intentToResult)
                        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

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