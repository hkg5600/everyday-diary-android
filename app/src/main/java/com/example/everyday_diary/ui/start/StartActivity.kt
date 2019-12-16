package com.example.everyday_diary.ui.start

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.example.everyday_diary.R
import com.example.everyday_diary.base.BaseActivity
import com.example.everyday_diary.databinding.ActivityStartBinding
import com.example.everyday_diary.ui.login.LoginFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class StartActivity : BaseActivity<ActivityStartBinding, StartActivityViewModel>() {
    override val layoutResourceId = R.layout.activity_start
    override val viewModel: StartActivityViewModel by viewModel()
    private val loginFragment: LoginFragment by inject()
    lateinit var transaction : FragmentTransaction

    override fun initView() {
        initFragment()
    }

    override fun initObserver() {

    }

    override fun initListener() {

    }

    override fun initViewModel() {

    }

    private fun initFragment() {
        transaction = supportFragmentManager.beginTransaction()
        transaction.replace(viewDataBinding.frameLayout.id, loginFragment).commitAllowingStateLoss()
    }
}
