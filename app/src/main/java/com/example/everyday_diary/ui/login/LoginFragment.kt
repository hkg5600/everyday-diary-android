package com.example.everyday_diary.ui.login

import android.content.Intent
import androidx.lifecycle.Observer
import com.example.everyday_diary.R
import com.example.everyday_diary.base.BaseFragment
import com.example.everyday_diary.databinding.FragmentLoginBinding
import com.example.everyday_diary.network.response.LoginResponse
import com.example.everyday_diary.room.model.Token
import com.example.everyday_diary.ui.main.MainActivity
import com.example.everyday_diary.utils.CustomTextWatcher
import com.example.everyday_diary.utils.TokenObject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginFragmentViewModel>() {
    override val layoutResourceId = R.layout.fragment_login
    override val viewModel: LoginFragmentViewModel by viewModel()

    override fun initView() {
        viewDataBinding.vm = viewModel
        viewDataBinding.buttonLogin.isEnabled = false
    }

    override fun initObserver() {
        viewModel.roomSuccess.observe(this, Observer {
            when (it) {
                "tokenData" -> {
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity?.finish()
                }
            }
        })

        viewModel.error.observe(this, Observer {
            when (it) {
                "아이디 또는 비밀번호가 일치하지 않습니다" -> makeToast(it, false)
            }
        })

        viewModel.data.observe(this, Observer {
            when (it) {
                is LoginResponse -> {
                    TokenObject.run {
                        token = it.token
                        refreshToken = it.refreshToken
                    }
                    viewModel.insertTokenToRoom(Token(1, it.token, it.refreshToken))
                }
            }
        })
    }

    override fun initListener() {

        viewDataBinding.editTextId.addTextChangedListener(CustomTextWatcher {
            toggleButtonState()
        })

        viewDataBinding.editTextPassword.addTextChangedListener(CustomTextWatcher {
            toggleButtonState()
        })
    }

    override fun initViewModel() {

    }

    private fun toggleButtonState() {
        viewDataBinding.buttonLogin.isEnabled =
            viewDataBinding.editTextId.text.isNotEmpty() && viewDataBinding.editTextPassword.text.isNotEmpty()
    }
}
