package com.example.everyday_diary.network.response

import com.example.everyday_diary.network.model.Diary

data class DiaryListResponse(
    val diary: ArrayList<Diary>
)