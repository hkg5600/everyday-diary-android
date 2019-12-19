package com.example.everyday_diary.network.api

import com.example.everyday_diary.network.response.DiaryListResponse
import com.example.everyday_diary.network.response.MonthCount
import com.example.everyday_diary.network.response.Response
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface DiaryApi {

    @GET
    fun getDiaryCount(@Header("Authorization") token: String, @Url url: String): Single<retrofit2.Response<Response<MonthCount>>>

    @GET
    fun getDiaryByDate(@Header("Authorization") token: String, @Url url: String): Single<retrofit2.Response<Response<DiaryListResponse>>>

    @Multipart
    @POST("/diary/diary/")
    fun writeDiary(
        @Header("Authorization") token: String, @Part("title") title: RequestBody, @Part("text") text: RequestBody, @Part file: ArrayList<MultipartBody.Part>
        , @Part("owner") owner: RequestBody, @Part("month") month: RequestBody, @Part("year") year: RequestBody
    ) : Single<retrofit2.Response<Response<Any>>>
}