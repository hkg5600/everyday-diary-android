package com.example.everyday_diary.ui.main

import com.example.everyday_diary.base.BaseViewModel
import com.example.everyday_diary.network.service.DiaryService
import com.example.everyday_diary.utils.TokenObject

class MainActivityViewModel(private val service: DiaryService) : BaseViewModel() {

    fun getDiaryCount(id: Int) = addDisposable(service.getDiaryCount(TokenObject.tokenData(), id), getDataObserver())
}