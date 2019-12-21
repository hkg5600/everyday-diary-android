package com.example.everyday_diary.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.everyday_diary.R
import com.example.everyday_diary.ui.main.MainActivity

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        makeNotification(context)
    }

    private fun makeNotification(context: Context) {
        val notificationManager =
            NotificationManagerCompat.from(context)

        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.putExtra("notificationId", 100)
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, "alarm")
            .setContentTitle("everyday diary")
            .setContentText("Write your today")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            builder.setSmallIcon(R.drawable.ic_launcher_foreground) //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            val channelName = "notification channel"
            val description = "for upper oreo"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel("alarm", channelName, importance)
            channel.description = description

            notificationManager.createNotificationChannel(channel)

        } else builder.setSmallIcon(R.mipmap.ic_launcher)

        notificationManager.notify(1234, builder.build()) // 고유숫자로 노티피케이션 동작시킴

    }
}