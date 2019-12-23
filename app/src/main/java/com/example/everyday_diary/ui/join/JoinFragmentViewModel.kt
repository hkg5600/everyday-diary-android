package com.example.everyday_diary.ui.join

import androidx.databinding.ObservableField
import com.example.everyday_diary.base.BaseViewModel
import com.example.everyday_diary.network.reqeust.RegisterRequest
import com.example.everyday_diary.network.service.UserService

class JoinFragmentViewModel(private val service: UserService) : BaseViewModel() {

    val id = ObservableField<String>()
    val pw = ObservableField<String>()
    val pwAgain = ObservableField<String>()

    fun join() = addDisposable(service.join(RegisterRequest(id.get()!!, pw.get()!!, pwAgain.get()!!)), getMsgObserver())
}
