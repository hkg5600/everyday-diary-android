package com.example.everyday_diary.ui.start

import android.graphics.Color
import android.view.WindowManager
import androidx.fragment.app.FragmentTransaction
import com.example.everyday_diary.R
import com.example.everyday_diary.base.BaseActivity
import com.example.everyday_diary.databinding.ActivityStartBinding
import com.example.everyday_diary.ui.join.JoinFragment
import com.example.everyday_diary.ui.login.LoginFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class StartActivity : BaseActivity<ActivityStartBinding, StartActivityViewModel>() {
    override val layoutResourceId = R.layout.activity_start
    override val viewModel: StartActivityViewModel by viewModel()
    private val loginFragment: LoginFragment by inject()
    private val joinFragment: JoinFragment by inject()
    private lateinit var transaction: FragmentTransaction

    override fun initView() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        viewDataBinding.activity = this
        changeToLogin()
    }

    override fun initObserver() {

    }

    override fun initListener() {

    }

    override fun initViewModel() {

    }

    fun changeToLogin() {
        transaction = supportFragmentManager.beginTransaction()
        transaction.replace(viewDataBinding.frameLayout.id, loginFragment).commitAllowingStateLoss()
        viewDataBinding.textViewSignIn.setTextColor(Color.BLACK)
        viewDataBinding.textViewSignUp.setTextColor(Color.GRAY)
    }

    fun changeToJoin() {
        transaction = supportFragmentManager.beginTransaction()
        transaction.replace(viewDataBinding.frameLayout.id, joinFragment).commitAllowingStateLoss()
        viewDataBinding.textViewSignIn.setTextColor(Color.GRAY)
        viewDataBinding.textViewSignUp.setTextColor(Color.BLACK)
    }
}
