package com.example.everyday_diary.network.api

import com.example.travelercommunityapp.network.request.LoginRequest
import com.example.travelercommunityapp.network.request.RegisterRequest
import com.example.travelercommunityapp.network.request.TokenRequest
import com.example.travelercommunityapp.network.response.LoginResponse
import com.example.travelercommunityapp.network.response.Response
import com.example.travelercommunityapp.network.response.TokenResponse
import com.example.travelercommunityapp.network.response.UserInfoResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApi {

    @POST("/api/login/")
    fun login(@Body login: LoginRequest) : Single<retrofit2.Response<Response<LoginResponse>>>

    @POST("/api/verify/")
    fun verifyToken(@Body token: TokenRequest) : Single<retrofit2.Response<Response<Any>>>

    @POST("/api/refresh/")
    fun refreshToken(@Body token: TokenRequest) : Single<retrofit2.Response<Response<TokenResponse>>>

    @GET("/api/user/")
    fun getUserInfo(@Header("Authorization") token: String) : Single<retrofit2.Response<Response<UserInfoResponse>>>

    @POST("/api/join/")
    fun join(@Body register: RegisterRequest) : Single<retrofit2.Response<Response<Any>>>
}