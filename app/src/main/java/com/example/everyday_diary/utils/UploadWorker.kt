package com.example.everyday_diary.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.everyday_diary.network.response.Response
import com.example.everyday_diary.network.service.DiaryService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject


class UploadWorker(val context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters), KoinComponent {

    lateinit var video : MultipartBody.Part
    private val service: DiaryService by inject()
    private  val compositeDisposable = CompositeDisposable()

    override suspend fun doWork(): Result {
        val filePath = inputData.getString("value")
        setForeground(ForegroundInfo(5, NotificationUtil.getNotification(context, "업로드 중...", "Video Upload")))
        video = FileUtil.loadFile2(filePath!!, context)
        compositeDisposable.add(service.writeDiary2(
            TokenObject.tokenData(),
            createRequestBody("Test"),
            createRequestBody("Test"),
            video,
            createRequestBody(UserObject.user?.username!!),
            createRequestBody("3"),
            createRequestBody("2020")
        ).subscribeWith(object : DisposableSingleObserver<retrofit2.Response<Response<String>>>() {
            override fun onSuccess(t: retrofit2.Response<Response<String>>) {
                //NotificationUtil.deleteNotification(context, 2)
                if (t.isSuccessful) {
                    if(t.body()?.message == "success") {
                        NotificationUtil.makeNotification(context, "업로드 성공", "Video Upload", 3)
                    } else {
                        NotificationUtil.makeNotification(context, "업로드 실패", "Video Upload", 3)
                    }
                } else {
                    NotificationUtil.makeNotification(context, "업로드 실패", "Video Upload", 3)
                }
            }

            override fun onError(e: Throwable) {
                NotificationUtil.makeNotification(context, "업로드 실패", "Video Upload", 3)
            }
        }))

        compositeDisposable.dispose()
        return Result.success()
    }

//    override fun onStopped() {
//        NotificationUtil.makeNotification(context, "업로드가 중지되었습니다", "Video Upload", 3)
//        super.onStopped()
//    }


    private fun createRequestBody(data: String) = RequestBody.create(MediaType.parse("inputText/plain"), data)
}