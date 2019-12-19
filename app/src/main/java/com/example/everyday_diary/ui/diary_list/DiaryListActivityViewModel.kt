package com.example.everyday_diary.ui.diary_list

import com.example.everyday_diary.base.BaseViewModel
import com.example.everyday_diary.network.service.DiaryService
import com.example.everyday_diary.utils.TokenObject

class DiaryListActivityViewModel(private val service: DiaryService) : BaseViewModel() {

    fun getDiaryByMonth(id: Int) = addDisposable(service.getDiaryByDate(TokenObject.tokenData(), id), getDataObserver())
}