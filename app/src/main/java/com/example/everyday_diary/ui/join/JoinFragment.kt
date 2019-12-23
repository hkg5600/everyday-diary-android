package com.example.everyday_diary.ui.join

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.everyday_diary.R
import com.example.everyday_diary.base.BaseFragment
import com.example.everyday_diary.databinding.FragmentJoinBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class JoinFragment : BaseFragment<FragmentJoinBinding, JoinFragmentViewModel>() {
    override val layoutResourceId = R.layout.fragment_join
    override val viewModel: JoinFragmentViewModel by viewModel()

    override fun initView() {

    }

    override fun initObserver() {
    }

    override fun initListener() {

    }

    override fun initViewModel() {

    }

}
