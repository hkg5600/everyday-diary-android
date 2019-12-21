package com.example.everyday_diary.ui.recent_diary

import com.example.everyday_diary.base.BaseViewModel
import com.example.everyday_diary.network.service.DiaryService
import com.example.everyday_diary.utils.TokenObject

class RecentDiaryActivityViewModel(private val service: DiaryService) : BaseViewModel() {
    fun getRecentDiary() = addDisposable(service.getRecentDiary(TokenObject.tokenData()),getDataObserver())
}