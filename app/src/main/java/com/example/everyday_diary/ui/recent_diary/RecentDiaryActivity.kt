package com.example.everyday_diary.ui.recent_diary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.everyday_diary.R
import com.example.everyday_diary.base.BaseActivity
import com.example.everyday_diary.databinding.ActivityRecentDiaryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecentDiaryActivity : BaseActivity<ActivityRecentDiaryBinding, RecentDiaryActivityViewModel>() {
    override val layoutResourceId = R.layout.activity_recent_diary
    override val viewModel: RecentDiaryActivityViewModel by viewModel()


    override fun initView() {

    }

    override fun initObserver() {

    }

    override fun initListener() {

    }

    override fun initViewModel() {

    }

}
