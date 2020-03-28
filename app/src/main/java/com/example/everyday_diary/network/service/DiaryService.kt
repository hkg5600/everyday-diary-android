package com.example.everyday_diary.network.service

import com.example.everyday_diary.network.api.DiaryApi
import com.example.everyday_diary.network.response.*
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody

interface DiaryService {
    fun writeDiary2(
        token: String,
        title: RequestBody,
        text: RequestBody,
        file: MultipartBody.Part,
        owner: RequestBody,
        month: RequestBody,
        year: RequestBody
    ) : Single<retrofit2.Response<Response<String>>>
    fun getDiaryCount(token: String, id: Int): Single<retrofit2.Response<Response<MonthCount>>>
    fun getDiaryByDate(
        token: String,
        month: Int,
        year: Int
    ): Single<retrofit2.Response<Response<DiaryListResponse>>>

    fun writeDiary(
        token: String,
        title: RequestBody,
        text: RequestBody,
        file: ArrayList<MultipartBody.Part>,
        owner: RequestBody,
        month: RequestBody,
        year: RequestBody
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
        month: RequestBody,
        year: RequestBody,
        owner: RequestBody,
        file: MultipartBody.Part
    ): Single<retrofit2.Response<Response<Any>>>

    fun getRecentDiary(token: String): Single<retrofit2.Response<Response<DiaryListResponse>>>
    fun getDiaryCount2(token: String, id: Int): Single<MonthCount?>
}

class DiaryServiceImpl(private val api: DiaryApi) : DiaryService {

    override fun getRecentDiary(token: String) = api.getRecentDiary(token)


    override fun postCardImage(
        token: String,
        month: RequestBody,
        year: RequestBody,
        owner: RequestBody,
        file: MultipartBody.Part
    ) = api.postCardImage(token, month, year, owner, file)

    override fun deleteCardImage(
        token: String,
        id: Int
    ) = api.deleteCardImage(token, "/diary/card-image/$id/")

    override fun getCardImage(
        token: String,
        year: Int
    ) = api.getCardImage(token, "/diary/card-image/$year/")

    override fun getDiaryDetail(
        token: String,
        id: Int
    ) = api.getDiaryDetail(token, "/diary/diary/$id/")

    override fun getDiaryByDate(token: String, month: Int, year: Int) =
        api.getDiaryByDate(token, "/diary/diary-month/$month/$year/")

    override fun writeDiary(
        token: String,
        title: RequestBody,
        text: RequestBody,
        file: ArrayList<MultipartBody.Part>,
        owner: RequestBody,
        month: RequestBody,
        year: RequestBody
    ) = api.writeDiary(token, title, text, file, owner, month, year)

    override fun writeDiary2(
        token: String,
        title: RequestBody,
        text: RequestBody,
        file: MultipartBody.Part,
        owner: RequestBody,
        month: RequestBody,
        year: RequestBody
    ) = api.writeDiary2(token, title, text, file, owner, month, year)

    override fun getDiaryCount(token: String, id: Int) =
        api.getDiaryCount(token, "/diary/month/$id/")
    override fun getDiaryCount2(
        token: String,
        id: Int
    ) = api.getDiaryCount(token, "/diary/month/$id/").map {
        MonthCountFilter.getFilteredData(it)
    }
}