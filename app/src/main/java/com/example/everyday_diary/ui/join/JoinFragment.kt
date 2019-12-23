package com.example.everyday_diary.ui.join

import androidx.lifecycle.Observer

import com.example.everyday_diary.R
import com.example.everyday_diary.base.BaseFragment
import com.example.everyday_diary.databinding.FragmentJoinBinding
import com.example.everyday_diary.utils.CustomTextWatcher
import org.koin.androidx.viewmodel.ext.android.viewModel

class JoinFragment : BaseFragment<FragmentJoinBinding, JoinFragmentViewModel>() {
    override val layoutResourceId = R.layout.fragment_join
    override val viewModel: JoinFragmentViewModel by viewModel()

    override fun initView() {
        viewDataBinding.vm = viewModel
        viewDataBinding.buttonJoin.isEnabled = false
    }

    override fun initObserver() {

        viewModel.message.observe(this, Observer {
            when (it) {
                "회원가입 성공" -> {
                    makeToast("Success to join!", false)
                    activity?.recreate()
                }
            }
        })
        viewModel.error.observe(this, Observer {
            when (it) {
                "이미 존재하는 사용자 이름입니다" -> makeToast("username already exists", false)
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
        viewDataBinding.editTextPasswordAgain.addTextChangedListener(CustomTextWatcher {
            toggleButtonState()
            setButtonStatByPwIsSame()
        })
    }

    override fun initViewModel() {

    }

    private fun toggleButtonState() {
        viewDataBinding.buttonJoin.isEnabled =
            viewDataBinding.editTextId.text.isNotEmpty() && viewDataBinding.editTextPassword.text.isNotEmpty() && viewDataBinding.editTextPasswordAgain.text.isNotEmpty()
    }

    private fun setButtonStatByPwIsSame() {
        if (viewDataBinding.editTextPassword.text.toString() == viewDataBinding.editTextPasswordAgain.text.toString()) {
            viewDataBinding.editTextPasswordAgain.error = null
            viewDataBinding.buttonJoin.isEnabled = true
        } else {
            viewDataBinding.editTextPasswordAgain.error = "Enter the same password"
            viewDataBinding.buttonJoin.isEnabled = false
        }
    }
}
