package com.example.everyday_diary.utils

import android.app.Application
import com.example.everyday_diary.di.myDiModule
import org.koin.android.ext.android.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, myDiModule)
    }
}