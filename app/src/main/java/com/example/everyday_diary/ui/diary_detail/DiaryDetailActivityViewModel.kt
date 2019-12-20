package com.example.everyday_diary.ui.diary_detail

import com.example.everyday_diary.base.BaseViewModel
import com.example.everyday_diary.network.service.DiaryService
import com.example.everyday_diary.utils.TokenObject

class DiaryDetailActivityViewModel(private val service: DiaryService) : BaseViewModel() {

    fun getDiaryDetail(id: Int) = addDisposable(service.getDiaryDetail(TokenObject.tokenData(), id), getDataObserver())

}