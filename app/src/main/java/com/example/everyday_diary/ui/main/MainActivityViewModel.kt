package com.example.everyday_diary.ui.main

import com.example.everyday_diary.base.BaseViewModel
import com.example.everyday_diary.network.service.DiaryService
import com.example.everyday_diary.network.service.UserService
import com.example.everyday_diary.utils.TokenObject
import com.example.everyday_diary.utils.UserObject
import okhttp3.MultipartBody

class MainActivityViewModel(
    private val diaryService: DiaryService,
    private val userService: UserService
) : BaseViewModel() {

    fun getDiaryCount(id: Int) =
        addDisposable(diaryService.getDiaryCount(TokenObject.tokenData(), id), getDataObserver())

    fun getUserInfo() =
        addDisposable(userService.getUserInfo(TokenObject.tokenData()), getDataObserver())

    fun logout() {
        TokenObject.refreshToken = null
        TokenObject.token = null
        UserObject.user = null
        deleteTokenFromRoom()
    }

    fun getCardImage(year: Int) =
        addDisposable(diaryService.getCardImage(TokenObject.tokenData(), year), getDataObserver())

    fun deleteCardImage(id: Int) =
        addDisposable(diaryService.deleteCardImage(TokenObject.tokenData(), id), getMsgObserver())

    fun postCardImage(month: String, year: String, file: MultipartBody.Part) =
        addDisposable(
            diaryService.postCardImage(
                TokenObject.tokenData(),
                month,
                year,
                UserObject.user?.username!!,
                file
            ), getMsgObserver()
        )
}