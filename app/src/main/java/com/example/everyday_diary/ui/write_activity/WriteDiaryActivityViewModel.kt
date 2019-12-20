package com.example.everyday_diary.ui.write_activity

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.databinding.ObservableField
import com.example.everyday_diary.adapter.GalleryImageAdapter
import com.example.everyday_diary.base.BaseViewModel
import com.example.everyday_diary.network.service.DiaryService
import com.example.everyday_diary.utils.FileUtil
import com.example.everyday_diary.utils.TokenObject
import com.example.everyday_diary.utils.UserObject
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class WriteDiaryActivityViewModel(private val service: DiaryService) : BaseViewModel() {

    var fileList = ArrayList<MultipartBody.Part>()
    var title = ObservableField<String>()
    var text = ObservableField<String>()
    var month = ""
    var year = ""

    fun writeDiary() = addDisposable(
        service.writeDiary(
            TokenObject.tokenData(),
            title.get()!!,
            text.get()!!,
            fileList,
            UserObject.user?.username!!,
            month,
            year
        ), getMsgObserver()
    )


}