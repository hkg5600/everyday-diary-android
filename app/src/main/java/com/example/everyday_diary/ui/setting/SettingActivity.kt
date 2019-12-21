package com.example.everyday_diary.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.everyday_diary.R
import com.example.everyday_diary.base.BaseActivity
import com.example.everyday_diary.databinding.ActivitySettingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingActivity : BaseActivity<ActivitySettingBinding, SettingActivityViewModel>() {
    override val layoutResourceId = R.layout.activity_setting
    override val viewModel: SettingActivityViewModel by viewModel()

    override fun initView() {

    }

    override fun initObserver() {

    }

    override fun initListener() {

    }

    override fun initViewModel() {
        
    }


}
