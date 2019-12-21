package com.example.everyday_diary.utils

import android.annotation.SuppressLint
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService

@SuppressLint("Registered")
class Service : JobService() {


    override fun onStartJob(job: JobParameters): Boolean {

        return false
    }

    override fun onStopJob(job: JobParameters): Boolean {
        return false
    }
}