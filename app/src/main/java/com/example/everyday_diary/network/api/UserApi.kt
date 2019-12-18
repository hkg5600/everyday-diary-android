package com.example.everyday_diary.network.api

import com.example.everyday_diary.network.reqeust.LoginRequest
import com.example.everyday_diary.network.reqeust.RegisterRequest
import com.example.everyday_diary.network.reqeust.TokenRequest
import com.example.everyday_diary.network.response.LoginResponse
import com.example.everyday_diary.network.response.Response
import com.example.everyday_diary.network.response.TokenResponse
import com.example.everyday_diary.network.response.UserInfoResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApi {

    @POST("/api/diary/login/")
    fun login(@Body login: LoginRequest) : Single<retrofit2.Response<Response<LoginResponse>>>

    @POST("/api/diary/verify/")
    fun verifyToken(@Body token: TokenRequest) : Single<retrofit2.Response<Response<Any>>>

    @POST("/api/diary/refresh/")
    fun refreshToken(@Body token: TokenRequest) : Single<retrofit2.Response<Response<TokenResponse>>>

    @GET("/api/diary/user/")
    fun getUserInfo(@Header("Authorization") token: String) : Single<retrofit2.Response<Response<UserInfoResponse>>>

    @POST("/api/diary/join/")
    fun join(@Body register: RegisterRequest) : Single<retrofit2.Response<Response<Any>>>
}