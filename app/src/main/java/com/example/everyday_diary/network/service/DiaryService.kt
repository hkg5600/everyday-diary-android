package com.example.everyday_diary.network.service

import com.example.everyday_diary.network.api.DiaryApi
import com.example.everyday_diary.network.response.MonthCount
import com.example.everyday_diary.network.response.Response
import io.reactivex.Single

interface DiaryService {
    fun getDiaryCount(token: String, id: Int) : Single<retrofit2.Response<Response<MonthCount>>>
}

class DiaryServiceImpl(private val api: DiaryApi) : DiaryService {

    override fun getDiaryCount(token: String, id: Int) = api.getDiaryCount(token, "/diary/month/$id/")

}