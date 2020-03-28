package com.example.everyday_diary.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.everyday_diary.R

object NotificationUtil {

    fun getNotification(context:Context, msg: String, title: String) : Notification {
        val pendingIntent =
            PendingIntent.getActivities(context, 0, arrayOf(Intent()), PendingIntent.FLAG_UPDATE_CURRENT)
        val channelId = "TEST123"
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(msg)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "CHANNEL_NAME"
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        return notificationBuilder.build()
    }

    fun makeNotification(context: Context, msg: String, title: String,id: Int) {
        val pendingIntent =
            PendingIntent.getActivities(context, 0, arrayOf(Intent()), PendingIntent.FLAG_UPDATE_CURRENT)
        val channelId = "TEST123"
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(msg)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "CHANNEL_NAME"
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(id, notificationBuilder.build())
    }

    fun deleteNotification(context: Context, id: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(id)
    }
}