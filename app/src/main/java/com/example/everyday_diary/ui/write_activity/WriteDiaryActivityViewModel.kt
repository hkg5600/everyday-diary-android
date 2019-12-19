package com.example.everyday_diary.ui.write_activity

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.databinding.ObservableField
import com.example.everyday_diary.adapter.GalleryImageAdapter
import com.example.everyday_diary.base.BaseViewModel
import com.example.everyday_diary.network.service.DiaryService
import com.example.everyday_diary.utils.FileManager
import com.example.everyday_diary.utils.TokenObject
import com.example.travelercommunityapp.utils.UserObject
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

    fun getImageFromGallery(context: Activity): ArrayList<String> {
        val galleryImageUrls: ArrayList<String> = ArrayList()
        val columns = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID
        )//get all columns of type images
        val orderBy = MediaStore.Images.Media.DATE_TAKEN//order data by date

        val imageCursor = context.managedQuery(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
            null, null, "$orderBy DESC"
        )

        for (i in 0 until imageCursor.count) {
            imageCursor.moveToPosition(i)
            val dataColumnIndex =
                imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)//get column index
            galleryImageUrls.add(imageCursor.getString(dataColumnIndex))//get Image from column index

        }
        return galleryImageUrls
    }

    private fun loadFile(imageList: ArrayList<GalleryImageAdapter.Image>, context: Context) {
        if (imageList.isNotEmpty()) {
            imageList.run {
                this.forEach {
                    val file = File(
                        FileManager.getRealPathFromURI(
                            Uri.parse(it.uri),
                            context
                        )!!
                    )
                    if (file.exists()) {
                        val requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), file)
                        val multipartData = MultipartBody.Part.createFormData(
                            "image_${this.indexOf(it)}",
                            "file.jpg",
                            requestFile
                        )
                        fileList.add(multipartData)
                    }
                }
            }
        }
    }

}