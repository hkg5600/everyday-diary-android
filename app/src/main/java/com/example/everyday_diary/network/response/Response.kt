package com.example.everyday_diary.network.response

data class Response<T>(val data: T, val status: Int, val message: String)