package com.example.everyday_diary.di

import com.example.everyday_diary.adapter.DiaryListAdapter
import com.example.everyday_diary.adapter.DiaryWriteImageAdapter
import com.example.everyday_diary.adapter.GalleryImageAdapter
import com.example.everyday_diary.adapter.MonthAdapter
import com.example.everyday_diary.network.api.DiaryApi
import com.example.everyday_diary.network.api.UserApi
import com.example.everyday_diary.network.service.DiaryService
import com.example.everyday_diary.network.service.DiaryServiceImpl
import com.example.everyday_diary.network.service.UserService
import com.example.everyday_diary.network.service.UserServiceImpl
import com.example.everyday_diary.ui.diary_detail.DiaryDetailActivityViewModel
import com.example.everyday_diary.ui.diary_list.DiaryListActivityViewModel
import com.example.everyday_diary.ui.login.LoginFragment
import com.example.everyday_diary.ui.login.LoginFragmentViewModel
import com.example.everyday_diary.ui.main.MainActivityViewModel
import com.example.everyday_diary.ui.splash.SplashActivityViewModel
import com.example.everyday_diary.ui.start.StartActivityViewModel
import com.example.everyday_diary.ui.write_activity.WriteDiaryActivityViewModel
import com.example.everyday_diary.utils.BASE_URL
import com.example.everyday_diary.utils.TokenUtil
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

//val httpClient : OkHttpClient = OkHttpClient.Builder()
//    .connectTimeout(60, TimeUnit.SECONDS) // connect timeout
//    .writeTimeout(60, TimeUnit.SECONDS) // write timeout
//    .readTimeout(60, TimeUnit.SECONDS)
//    .build()// read timeout

val retrofit: Retrofit = Retrofit
    .Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build()

private val userApi: UserApi = retrofit.create(UserApi::class.java)
private val diaryApi: DiaryApi = retrofit.create(DiaryApi::class.java)

val networkModule = module {
    single { userApi }
    single { diaryApi }
}

var serviceModel = module {
    factory<UserService> { UserServiceImpl(get()) }
    factory<DiaryService> { DiaryServiceImpl(get()) }
}

var viewModelPart = module {
    viewModel { SplashActivityViewModel(get()) }
    viewModel { StartActivityViewModel() }
    viewModel { MainActivityViewModel(get(), get()) }
    viewModel { LoginFragmentViewModel(get()) }
    viewModel { DiaryListActivityViewModel(get()) }
    viewModel { WriteDiaryActivityViewModel(get()) }
    viewModel { DiaryDetailActivityViewModel() }
}

var adapterPart = module {
    factory { MonthAdapter() }
    factory { DiaryListAdapter() }
    factory { GalleryImageAdapter() }
    factory { DiaryWriteImageAdapter() }
}

var repositoryPart = module {

}

var tokenPart = module {
    single { TokenUtil(get(), get()) }
}

var fragmentPart = module {
    factory { LoginFragment() }
}

var myDiModule = listOf(
    viewModelPart,
    networkModule,
    serviceModel,
    adapterPart,
    repositoryPart,
    tokenPart,
    fragmentPart
)