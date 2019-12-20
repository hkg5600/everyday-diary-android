package com.example.everyday_diary.ui.splash

import com.example.everyday_diary.network.service.UserService
import com.example.everyday_diary.room.model.Token
import com.example.everyday_diary.base.BaseViewModel
import com.example.everyday_diary.network.reqeust.TokenRequest

class SplashActivityViewModel(private val userService: UserService) : BaseViewModel() {

    fun verifyToken(token: String) =
        addDisposable(userService.verifyToken(TokenRequest(token)), getMsgObserver())

    fun refreshToken(token: String) =
        addDisposable(userService.refreshToken(TokenRequest(token)), getDataObserver())

    fun insertToken(token: String, refreshToken: String) =
        insertToken(Token(1, token, refreshToken))
}