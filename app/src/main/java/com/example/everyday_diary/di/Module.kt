package com.example.everyday_diary.di

import com.example.everyday_diary.ui.splash.SplashActivityViewModel
import com.example.everyday_diary.utils.BASE_URL
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



val networkModule = module {

}

var serviceModel = module {

}

var viewModelPart = module {
    viewModel { SplashActivityViewModel(get()) }
}

var adapterPart = module {

}

var repositoryPart = module {

}

var tokenPart = module {

}

var fragmentPart = module {

}

var myDiModule = listOf(viewModelPart, networkModule, serviceModel, adapterPart, repositoryPart, tokenPart, fragmentPart)