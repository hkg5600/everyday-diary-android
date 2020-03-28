package com.example.everyday_diary.network.response

import com.example.everyday_diary.utils.NetworkFilter
import com.example.everyday_diary.utils.NetworkFilterImpl

data class MonthCount(
    val count: ArrayList<Int>
)

object MonthCountFilter : NetworkFilterImpl<MonthCount> by NetworkFilter() {
    fun getFilteredData(response: retrofit2.Response<Response<MonthCount>>): MonthCount? {
        return filterWithData(response)
    }
}