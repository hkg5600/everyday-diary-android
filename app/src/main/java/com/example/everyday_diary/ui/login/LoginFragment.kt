package com.example.everyday_diary.ui.login

import com.example.everyday_diary.R
import com.example.everyday_diary.base.BaseFragment
import com.example.everyday_diary.databinding.FragmentLoginBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginFragmentViewModel>() {
    override val layoutResourceId = R.layout.fragment_login
    override val viewModel: LoginFragmentViewModel by viewModel()

    override fun initView() {

    }

    override fun initObserver() {

    }

    override fun initListener() {

    }

    override fun initViewModel() {

    }


}
