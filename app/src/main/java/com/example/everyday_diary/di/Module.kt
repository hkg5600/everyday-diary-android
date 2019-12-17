package com.example.everyday_diary.di

import com.example.everyday_diary.adapter.MonthAdapter
import com.example.everyday_diary.network.api.UserApi
import com.example.everyday_diary.network.service.UserService
import com.example.everyday_diary.network.service.UserServiceImpl
import com.example.everyday_diary.ui.login.LoginFragment
import com.example.everyday_diary.ui.login.LoginFragmentViewModel
import com.example.everyday_diary.ui.main.MainActivityViewModel
import com.example.everyday_diary.ui.splash.SplashActivityViewModel
import com.example.everyday_diary.ui.start.StartActivityViewModel
import com.example.everyday_diary.utils.BASE_URL
import com.example.everyday_diary.utils.TokenManager
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


val retrofit: Retrofit = Retrofit
    .Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build()

private val userApi: UserApi = retrofit.create(UserApi::class.java)

val networkModule = module {
    single { userApi }
}

var serviceModel = module {
    factory<UserService> { UserServiceImpl(get()) }
}

var viewModelPart = module {
    viewModel { SplashActivityViewModel(get()) }
    viewModel { StartActivityViewModel() }
    viewModel { MainActivityViewModel() }
    viewModel { LoginFragmentViewModel(get()) }
}

var adapterPart = module {
    factory { MonthAdapter() }
}

var repositoryPart = module {

}

var tokenPart = module {
    single { TokenManager(get(), get()) }
}

var fragmentPart = module {
    factory { LoginFragment() }
}

var myDiModule = listOf(viewModelPart, networkModule, serviceModel, adapterPart, repositoryPart, tokenPart, fragmentPart)