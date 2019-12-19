package com.example.everyday_diary.network.service

import com.example.everyday_diary.network.api.DiaryApi
import com.example.everyday_diary.network.response.DiaryListResponse
import com.example.everyday_diary.network.response.MonthCount
import com.example.everyday_diary.network.response.Response
import io.reactivex.Single

interface DiaryService {
    fun getDiaryCount(token: String, id: Int): Single<retrofit2.Response<Response<MonthCount>>>
    fun getDiaryByMonth(token: String, id: Int): Single<retrofit2.Response<Response<DiaryListResponse>>>
}

class DiaryServiceImpl(private val api: DiaryApi) : DiaryService {
    override fun getDiaryByMonth(token: String, id: Int) = api.getDiaryByDate(token, "/api/diary/diary-month/$id/")
    override fun getDiaryCount(token: String, id: Int) = api.getDiaryCount(token, "/api/diary/month/$id/")
}