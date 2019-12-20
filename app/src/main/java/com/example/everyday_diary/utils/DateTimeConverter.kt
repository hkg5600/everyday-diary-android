package com.example.everyday_diary.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

object DateTimeConverter {
    @SuppressLint("SimpleDateFormat")
    fun jsonTimeToTime(jsonTime: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm")
        return formatter.format(parser.parse(jsonTime)!!)
    }

    fun monthToString(month: Int) = when (month) {
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

    fun stringToMonth(month: String) = when (month) {
        "Jan" -> 1
        "Feb" -> 2
        "Mar" -> 3
        "Apr" -> 4
        "May" -> 5
        "Jun" -> 6
        "Jul" -> 7
        "Aug" -> 8
        "Sep" -> 9
        "Oct" -> 10
        "Nov" -> 11
        "Dec" -> 12
        else -> 1
    }

    @SuppressLint("SimpleDateFormat")
    fun dateTimeToDay(stringDate: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getDayFromLocalDate(LocalDate.parse(stringDate))
        } else {
            getDayFromDate(SimpleDateFormat("yyy-MM-dd").parse(stringDate)!!)
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun getDayFromLocalDate(date: LocalDate) = date.dayOfMonth.toString()

    private fun getDayFromDate(date: Date) = date.day.toString()

    @SuppressLint("SimpleDateFormat")
    fun getWeekOfDate(stringDate: String) : String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWeekOfDateFromLocalDate(LocalDate.parse(stringDate)).substring(0, 3)
        } else {
            getWeekOfDateFromDate(SimpleDateFormat("yyy-MM-dd").parse(stringDate)!!).substring(0, 3)
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun getWeekOfDateFromLocalDate(date: LocalDate) = date.dayOfWeek.toString()

    private fun getWeekOfDateFromDate(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.DAY_OF_WEEK).toString()
    }
}