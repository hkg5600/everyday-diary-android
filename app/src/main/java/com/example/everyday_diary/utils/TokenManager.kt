package com.example.everyday_diary.utils

import android.app.Application
import com.example.everyday_diary.network.service.UserService
import com.example.everyday_diary.room.model.Token
import com.example.everyday_diary.room.repository.TokenRepository
import com.example.everyday_diary.network.reqeust.TokenRequest
import org.koin.standalone.KoinComponent
import org.koin.standalone.get

class TokenManager(private val userService: UserService, val application: Application) {

    init {
        useService = userService
        tokenRepository = TokenRepository(application)
    }

    companion object : KoinComponent {
        private var INSTANCE : TokenManager? = null
        var tokenRepository : TokenRepository? = null
        var useService: UserService? = null
        @Synchronized
        fun getInstance(): TokenManager? {
            if (INSTANCE == null) {
                INSTANCE = get()
            }
            return INSTANCE
        }
    }

    fun getToken() = tokenRepository?.getToken()
    fun insertToken(token: Token) = tokenRepository?.insertToken(token)
    fun refreshToken(token: String) = userService.refreshToken(TokenRequest(token))
    fun deleteToken() = tokenRepository?.deleteToken()
}