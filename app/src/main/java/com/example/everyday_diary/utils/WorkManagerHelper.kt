package com.example.everyday_diary.utils

import android.app.ActivityManager
import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.paulinasadowska.rxworkmanagerobservers.extensions.getWorkDataByIdSingle

object WorkManagerHelper {

    private lateinit var dataRequest: OneTimeWorkRequest

    fun makeWorkManagerInstance(context: Context, str: String) {
        dataRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setInputData(
                Data.Builder().putString(
                    "value",
                    str
                ).build()
            ).build()
        WorkManager.getInstance(context).enqueue(dataRequest)
    }

    private fun observeWorkerManagerInstance(context: Context) {
        WorkManager.getInstance().getWorkDataByIdSingle(dataRequest.id)
            .subscribe(
                { data ->
                    Log.e("Data", "${data.getString("msg")}")
                    NotificationUtil.makeNotification(context, "업로드 성공!", "Video Upload", 1)
                    closeNotification(context)
                },
                { error ->
                    Log.e("Error", "${error.message}")
                    NotificationUtil.makeNotification(context, "업로드 실패", "Video Upload", 1)
                    closeNotification(context)
                })
    }

    private fun closeNotification(context: Context) {
        NotificationUtil.deleteNotification(context, 2)
    }

}