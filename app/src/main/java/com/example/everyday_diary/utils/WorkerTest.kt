package com.example.everyday_diary.utils

import android.content.Context
import android.util.Log
import androidx.work.*

import com.example.everyday_diary.network.response.Response
import com.example.everyday_diary.network.service.DiaryService
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class WorkerTest(val context: Context, workerParameters: WorkerParameters) :
    RxWorker(context, workerParameters), KoinComponent {

    var fileList = ArrayList<MultipartBody.Part>()
    lateinit var video : MultipartBody.Part
    private val service: DiaryService by inject()

    override fun createWork(): Single<Result> {
        val strList = inputData.getString("value")
        video = FileUtil.loadFile2(strList!!, context)
        fileList.add(FileUtil.loadFile2(strList!!, context))
        Log.e("MSG", "In WorkerTest Class")

        return service.writeDiary2(
            TokenObject.tokenData(),
            createRequestBody("Test"),
            createRequestBody("Test"),
            video,
            createRequestBody(UserObject.user?.username!!),
            createRequestBody("3"),
            createRequestBody("2020")
        ).map {
            t: retrofit2.Response<Response<String>> ->
            t.errorBody()?.close()
            val data = Data.Builder()
                .putString("msg", t.body()?.message)
                .build()
            Result.success(data)
        }

    }
    private inline fun <T: Collection<Any>> T.doOnNotEmpty(func: (T) -> Unit) {
        if (this.isNotEmpty()) {
            func(this)
        }
    }

    private fun createRequestBody(data: String) = RequestBody.create(MediaType.parse("inputText/plain"), data)



}