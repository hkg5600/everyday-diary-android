package com.example.everyday_diary.ui.diary_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.everyday_diary.R
import com.example.everyday_diary.base.BaseActivity
import com.example.everyday_diary.databinding.ActivityDiaryListBinding
import kotlinx.android.synthetic.main.app_bar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DiaryListActivity : BaseActivity<ActivityDiaryListBinding, DIaryListActivitViewModel>() {

    override val layoutResourceId = R.layout.activity_diary_list
    override val viewModel: DIaryListActivitViewModel by viewModel()

    override fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    override fun initObserver() {

    }

    override fun initListener() {

    }

    override fun initViewModel() {

    }

}
