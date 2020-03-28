package com.example.everyday_diary.utils

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.example.everyday_diary.network.response.Response
import com.example.everyday_diary.network.service.DiaryService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject


class MyService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        onTaskRemoved(intent)


        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {

        val restartServiceIntent = Intent(applicationContext, this.javaClass)
        restartServiceIntent.setPackage(packageName)
        startService(restartServiceIntent)
        Toast.makeText(applicationContext, "Removed", Toast.LENGTH_SHORT).show()
        super.onTaskRemoved(rootIntent)
    }
}
