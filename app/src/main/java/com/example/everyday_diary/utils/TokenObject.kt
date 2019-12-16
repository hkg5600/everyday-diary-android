package com.example.travelercommunityapp.utils

import com.example.travelercommunityapp.network.request.TokenRequest
import com.example.travelercommunityapp.network.service.UserService

object TokenObject {
    var token : String? = null
    var refreshToken: String? = null

    fun tokenData() = "Token $token"
}