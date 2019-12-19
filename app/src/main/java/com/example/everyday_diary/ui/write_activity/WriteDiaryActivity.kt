package com.example.everyday_diary.ui.write_activity

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.view.WindowManager
import com.example.everyday_diary.R
import com.example.everyday_diary.adapter.GalleryImageAdapter
import com.example.everyday_diary.base.BaseActivity
import com.example.everyday_diary.databinding.ActivityWriteDiaryBinding
import com.example.everyday_diary.databinding.CustomDialogBinding
import kotlinx.android.synthetic.main.app_bar.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class WriteDiaryActivity : BaseActivity<ActivityWriteDiaryBinding, WriteDiaryActivityViewModel>() {
    override val layoutResourceId = R.layout.activity_write_diary
    override val viewModel: WriteDiaryActivityViewModel by viewModel()
    private val imageAdapter : GalleryImageAdapter by inject()
    lateinit var dialog: Dialog
    lateinit var customDialogBinding: CustomDialogBinding

    override fun initView() {
        initActionBar()
        initDialog("TEST")
        dialog.show()
    }

    override fun initObserver() {

    }

    override fun initListener() {

    }

    override fun initViewModel() {
        imageAdapter.setImage(getImageFromGallery(this))
    }

    private fun initDialog(dialogText: String) {
        val customDialog = layoutInflater.inflate(R.layout.custom_dialog, null)
        customDialogBinding = CustomDialogBinding.inflate(layoutInflater, customDialog as ViewGroup, false)
        dialog = Dialog(this)
        dialog.setContentView(customDialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        customDialogBinding.textView.text = dialogText
    }

    private fun initActionBar() {
        title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_write_diary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.menu_save -> {

            }
        }
        return super.onOptionsItemSelected(item)
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

}
