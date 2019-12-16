package com.example.everyday_diary.utils

object TokenObject {
    var token : String? = null
    var refreshToken: String? = null

    fun tokenData() = "Token $token"
}