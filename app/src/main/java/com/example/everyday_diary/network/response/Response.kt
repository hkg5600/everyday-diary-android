package com.example.everyday_diary.network.response

data class Response<T>(val data: T, var status: Int, val message: String)