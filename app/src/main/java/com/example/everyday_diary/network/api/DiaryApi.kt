package com.example.everyday_diary.network.api

import com.example.everyday_diary.network.response.*
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
    @POST("/api/diary/diary/")
    fun writeDiary(
        @Header("Authorization") token: String, @Part("title") title: RequestBody, @Part("text") text: RequestBody, @Part file: ArrayList<MultipartBody.Part>
        , @Part("owner") owner: RequestBody, @Part("month") month: RequestBody, @Part("year") year: RequestBody
    ): Single<retrofit2.Response<Response<Any>>>

    @GET
    fun getDiaryDetail(@Header("Authorization") token: String, @Url url: String): Single<retrofit2.Response<Response<DiaryDetailReponse>>>

    @GET
    fun getCardImage(@Header("Authorization") token: String, @Url url: String): Single<retrofit2.Response<Response<CardImageResponse>>>

    @DELETE
    fun deleteCardImage(@Header("Authorization") token: String, @Url url: String): Single<retrofit2.Response<Response<Any>>>

    @Multipart
    @POST("/api/diary/card-image/")
    fun postCardImage(
        @Header("Authorization") token: String, @Part("month") month: RequestBody,
        @Part("year") year: RequestBody, @Part("owner") owner: RequestBody, @Part file: MultipartBody.Part
    ) : Single<retrofit2.Response<Response<Any>>>

    @GET("/api/diary/recent-diary/")
    fun getRecentDiary(@Header("Authorization") token: String): Single<retrofit2.Response<Response<DiaryListResponse>>>
}