package com.example.everyday_diary.ui.main

import com.example.everyday_diary.base.BaseViewModel
import com.example.everyday_diary.network.service.DiaryService
import com.example.everyday_diary.network.service.UserService
import com.example.everyday_diary.utils.TokenObject

class MainActivityViewModel(private val diaryService: DiaryService, private val userService: UserService) : BaseViewModel() {

    fun getDiaryCount(id: Int) = addDisposable(diaryService.getDiaryCount(TokenObject.tokenData(), id), getDataObserver())
    fun getUserInfo() = addDisposable(userService.getUserInfo(TokenObject.tokenData()), getDataObserver())
}