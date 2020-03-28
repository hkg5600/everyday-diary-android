package com.example.everyday_diary.utils

import com.example.everyday_diary.network.response.Response

interface NetworkFilterImpl<T> {
    fun filterWithData(response: retrofit2.Response<Response<T>>): T?
}

class NetworkFilter<T> : NetworkFilterImpl<T> {
    override fun filterWithData(response: retrofit2.Response<Response<T>>): T? {
        return if (response.isSuccessful) {
            if (response.body()?.status != 200) {
                response.body()?.data
            } else
                null
        } else {
            null
        }
    }

}