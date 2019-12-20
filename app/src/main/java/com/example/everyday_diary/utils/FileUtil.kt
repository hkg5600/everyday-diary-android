package com.example.everyday_diary.utils

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.example.everyday_diary.adapter.GalleryImageAdapter
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.nio.file.Files.isDirectory

object FileUtil {

    fun loadFile(imageList: ArrayList<GalleryImageAdapter.Image>, context: Context) : List<MultipartBody.Part> {
        lateinit var multipartData: MultipartBody.Part
        if (imageList.isNotEmpty()) {
                return imageList.map {
                    val file = File(getRealPathFromURI(Uri.parse(it.uri), context)!!)
                    if (file.exists()) {
                        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                        multipartData = MultipartBody.Part.createFormData("image_${imageList.indexOf(it)}", "file.jpg", requestFile)
                    }
                    multipartData
                }
            }
            return emptyList()
        }


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

    fun getRealPathFromURI(contentURI: Uri?, context: Context): String? {
        val result: String?
        if (contentURI == null)
            return null
        val cursor = context.contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    fun deleteCache(context: Context) {
        try {
            val dir = context.cacheDir
            deleteDir(dir)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            return dir.delete()
        } else return if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }

}
