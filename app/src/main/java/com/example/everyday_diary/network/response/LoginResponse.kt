package com.example.everyday_diary.network.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("refresh_token") val refreshToken: String
)