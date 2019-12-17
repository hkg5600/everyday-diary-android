package com.example.everyday_diary.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

object DateTimeConverter {
    @SuppressLint("SimpleDateFormat")
    fun jsonTimeToTime(jsonTime : String) : String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm")
        return formatter.format(parser.parse(jsonTime)!!)
    }

    fun monthToString(month: Int) = when(month) {
        1 -> "Jan"
        2 -> "Feb"
        3 -> "Mar"
        4 -> "Apr"
        5 -> "May"
        6 -> "Jun"
        7 -> "Jul"
        8 -> "Aug"
        9 -> "Sep"
        10 -> "Oct"
        11 -> "Nov"
        12 -> "Dec"
        else -> "Jan"
    }
}