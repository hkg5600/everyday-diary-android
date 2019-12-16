package com.example.everyday_diary.ui.login

import androidx.databinding.ObservableField
import com.example.everyday_diary.base.BaseViewModel
import com.example.everyday_diary.network.reqeust.LoginRequest
import com.example.everyday_diary.network.service.UserService

class LoginFragmentViewModel(private val service: UserService) : BaseViewModel() {

    val id: ObservableField<String> = ObservableField()
    val pw: ObservableField<String> = ObservableField()

    fun login() =
        addDisposable(service.login(LoginRequest(id.get()!!, pw.get()!!)), getDataObserver())
}