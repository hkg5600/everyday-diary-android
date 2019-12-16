package com.example.travelercommunityapp.network.response

data class Response<T>(val data: T, var status: Int, val message: String)