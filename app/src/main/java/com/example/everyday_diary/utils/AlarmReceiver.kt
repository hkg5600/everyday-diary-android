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
import com.example.everyday_diary.ui.splash.SplashActivity

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtil.makeNotification(context, "Write your today", "Diary",1234)
    }
}