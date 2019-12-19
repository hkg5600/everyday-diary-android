package com.example.everyday_diary.network.api

import com.example.everyday_diary.network.response.DiaryListResponse
import com.example.everyday_diary.network.response.MonthCount
import com.example.everyday_diary.network.response.Response
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url

interface DiaryApi {

    @GET
    fun getDiaryCount(@Header("Authorization") token: String, @Url url: String) : Single<retrofit2.Response<Response<MonthCount>>>

    @GET
    fun getDiaryByDate(@Header("Authorization") token: String, @Url url: String) : Single<retrofit2.Response<Response<DiaryListResponse>>>
}