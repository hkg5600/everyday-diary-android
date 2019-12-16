package com.example.everyday_diary.network.service

import com.example.everyday_diary.network.api.UserApi
import com.example.everyday_diary.network.reqeust.LoginRequest
import com.example.everyday_diary.network.reqeust.RegisterRequest
import com.example.everyday_diary.network.reqeust.TokenRequest
import com.example.everyday_diary.network.response.LoginResponse
import com.example.everyday_diary.network.response.Response
import com.example.everyday_diary.network.response.TokenResponse
import com.example.everyday_diary.network.response.UserInfoResponse
import io.reactivex.Single

interface UserService {
    fun login(loginRequest: LoginRequest) : Single<retrofit2.Response<Response<LoginResponse>>>
    fun verifyToken(tokenRequest: TokenRequest) : Single<retrofit2.Response<Response<Any>>>
    fun refreshToken(tokenRequest: TokenRequest) : Single<retrofit2.Response<Response<TokenResponse>>>
    fun getUserInfo(token: String) : Single<retrofit2.Response<Response<UserInfoResponse>>>
    fun join(registerRequest: RegisterRequest): Single<retrofit2.Response<Response<Any>>>
}

class UserServiceImpl(private val api: UserApi) : UserService {
    override fun join(registerRequest: RegisterRequest) = api.join(registerRequest)

    override fun getUserInfo(token: String) = api.getUserInfo(token)

    override fun refreshToken(tokenRequest: TokenRequest) = api.refreshToken(tokenRequest)

    override fun verifyToken(tokenRequest: TokenRequest) = api.verifyToken(tokenRequest)

    override fun login(loginRequest: LoginRequest) = api.login(loginRequest)

}