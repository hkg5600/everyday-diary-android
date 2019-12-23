package com.example.everyday_diary.network.service

import com.example.everyday_diary.network.api.DiaryApi
import com.example.everyday_diary.network.response.*
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface DiaryService {
    fun getDiaryCount(token: String, id: Int): Single<retrofit2.Response<Response<MonthCount>>>
    fun getDiaryByDate(
        token: String,
        month: Int,
        year: Int
    ): Single<retrofit2.Response<Response<DiaryListResponse>>>

    fun writeDiary(
        token: String,
        title: String,
        text: String,
        file: ArrayList<MultipartBody.Part>,
        owner: String,
        month: String,
        year: String
    ): Single<retrofit2.Response<Response<Any>>>

    fun getDiaryDetail(
        token: String,
        id: Int
    ): Single<retrofit2.Response<Response<DiaryDetailReponse>>>

    fun getCardImage(
        token: String,
        year: Int
    ): Single<retrofit2.Response<Response<CardImageResponse>>>

    fun deleteCardImage(
        token: String,
        id: Int
    ): Single<retrofit2.Response<Response<Any>>>

    fun postCardImage(
        token: String,
        month: String,
        year: String,
        owner: String,
        file: MultipartBody.Part
    ): Single<retrofit2.Response<Response<Any>>>

    fun getRecentDiary(token: String): Single<retrofit2.Response<Response<DiaryListResponse>>>
}

class DiaryServiceImpl(private val api: DiaryApi) : DiaryService {

    override fun getRecentDiary(token: String) = api.getRecentDiary(token)

    override fun postCardImage(
        token: String,
        month: String,
        year: String,
        owner: String,
        file: MultipartBody.Part
    ) = api.postCardImage(
        token,
        RequestBody.create(MediaType.parse("inputText/plain"), month),
        RequestBody.create(MediaType.parse("inputText/plain"), year),
        RequestBody.create(MediaType.parse("inputText/plain"), owner),
        file
    )

    override fun deleteCardImage(
        token: String,
        id: Int
    ) = api.deleteCardImage(token, "/api/diary/card-image/$id/")

    override fun getCardImage(
        token: String,
        year: Int
    ) = api.getCardImage(token, "/api/diary/card-image/$year/")

    override fun getDiaryDetail(
        token: String,
        id: Int
    ) = api.getDiaryDetail(token, "/api/diary/diary/$id/")

    override fun writeDiary(
        token: String,
        title: String,
        text: String,
        file: ArrayList<MultipartBody.Part>,
        owner: String,
        month: String,
        year: String
    ) = api.writeDiary(
        token,
        RequestBody.create(MediaType.parse("inputText/plain"), title),
        RequestBody.create(MediaType.parse("inputText/plain"), text),
        file,
        RequestBody.create(MediaType.parse("inputText/plain"), owner),
        RequestBody.create(MediaType.parse("inputText/plain"), month),
        RequestBody.create(MediaType.parse("inputText/plain"), year)
    )

    override fun getDiaryByDate(token: String, month: Int, year: Int) =
        api.getDiaryByDate(token, "/api/diary/diary-month/$month/$year/")

    override fun getDiaryCount(token: String, id: Int) =
        api.getDiaryCount(token, "/api/diary/month/$id/")
}