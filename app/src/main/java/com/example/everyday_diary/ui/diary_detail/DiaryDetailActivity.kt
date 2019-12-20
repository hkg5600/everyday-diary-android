package com.example.everyday_diary.ui.diary_detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.everyday_diary.R
import com.example.everyday_diary.base.BaseActivity
import com.example.everyday_diary.databinding.ActivityDiaryDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DiaryDetailActivity :
    BaseActivity<ActivityDiaryDetailBinding, DiaryDetailActivityViewModel>() {
    
    override val layoutResourceId = R.layout.activity_diary_detail
    override val viewModel: DiaryDetailActivityViewModel by viewModel()


    override fun initView() {

    }

    override fun initObserver() {

    }

    override fun initListener() {

    }

    override fun initViewModel() {

    }


}
