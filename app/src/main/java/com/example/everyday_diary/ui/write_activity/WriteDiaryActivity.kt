package com.example.everyday_diary.ui.write_activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.everyday_diary.R
import com.example.everyday_diary.adapter.DiaryWriteImageAdapter
import com.example.everyday_diary.adapter.GalleryImageAdapter
import com.example.everyday_diary.base.BaseActivity
import com.example.everyday_diary.databinding.ActivityWriteDiaryBinding
import com.example.everyday_diary.databinding.CustomDialogBinding
import com.example.everyday_diary.utils.CustomTextWatcher
import kotlinx.android.synthetic.main.app_bar.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class WriteDiaryActivity : BaseActivity<ActivityWriteDiaryBinding, WriteDiaryActivityViewModel>() {
    override val layoutResourceId = R.layout.activity_write_diary
    override val viewModel: WriteDiaryActivityViewModel by viewModel()
    private val imageAdapter: GalleryImageAdapter by inject()
    private lateinit var dialog: Dialog
    lateinit var customDialogBinding: CustomDialogBinding
    private var isOpen = false
    private val diaryWriteImageAdapter: DiaryWriteImageAdapter by inject()

    override fun initView() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        initActionBar()
        initDialog("Do you want to exit?")
        initRecyclerView()
        initViewPager()
        setDateData()
        viewDataBinding.activity = this
        viewDataBinding.vm = viewModel
    }

    override fun initObserver() {
        imageAdapter.overSize.observe(this, Observer {
            makeToast("You can select up to 13", false)
        })

        imageAdapter.onChanged.observe(this, Observer {
            title = if (imageAdapter.selectedImageList.isEmpty())
                "Gallery Image"
            else
                "${imageAdapter.selectedImageList.size} selected"
            invalidateOptionsMenu()
        })

        diaryWriteImageAdapter.showGalleryImage.observe(this, Observer {
            showImageList()
        })

        viewModel.message.observe(this, Observer {
            makeToast(it, false)
            when (it) {
                "success" -> {
                    loadingDialog.dismiss()
                    finish()
                }
            }
        })

        viewModel.networkError.observe(this, Observer {
            makeToast("check your network connection", false)
            loadingDialog.dismiss()
            finish()
        })
    }

    override fun initListener() {

        viewDataBinding.editTextTitle.addTextChangedListener(CustomTextWatcher {
            invalidateOptionsMenu()
        })

        viewDataBinding.editTextText.addTextChangedListener(CustomTextWatcher {
            invalidateOptionsMenu()
        })
    }

    override fun initViewModel() {
        imageAdapter.setImage(viewModel.getImageFromGallery(this))
    }


    private fun setDateData() {
        val month = intent.getStringExtra("month")
        val year = intent.getStringExtra("year")
        if (month == null || year == null)
            finish()
        else {
            viewModel.month = month
            viewModel.year = year
        }
    }

    fun setViewPagerImage() {
        if (imageAdapter.selectedImageList.isEmpty())
            hideSelectedImage()
        else {
            diaryWriteImageAdapter.setImageList(imageAdapter.selectedImageList)
            showSelectedImage()
        }
        hideImageList()
    }

    private fun showSelectedImage() {
        viewDataBinding.viewPager.visibility = View.VISIBLE
        viewDataBinding.wormDotsIndicator.visibility = View.VISIBLE
    }

    private fun hideSelectedImage() {
        viewDataBinding.viewPager.visibility = View.GONE
        viewDataBinding.wormDotsIndicator.visibility = View.GONE
    }

    fun showImageList() {
        hideKeyboard()
        title = if (imageAdapter.selectedImageList.isNotEmpty())
            "${imageAdapter.selectedImageList.size} selected"
        else
            "Gallery Image"
        invalidateOptionsMenu()
        isOpen = true
        setHomeDrawable()
        viewDataBinding.galleryImageHolder.run {
            visibility = View.VISIBLE
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.gallery_image_show))
        }
        viewDataBinding.fabClose.run {
            visibility = View.VISIBLE
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.gallery_image_show))
        }
    }

    private fun hideImageList() {
        imageAdapter.isClosing = true
        title = ""
        invalidateOptionsMenu()
        isOpen = false
        setHomeDrawable()
        viewDataBinding.fabClose.visibility = View.GONE
        viewDataBinding.galleryImageHolder.run {
            val animation = AnimationUtils.loadAnimation(context, R.anim.gallery_image_hide)
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    viewDataBinding.galleryImageHolder.visibility = View.GONE
                    imageAdapter.isClosing = false
                }

                override fun onAnimationStart(animation: Animation?) {}
            })
            startAnimation(animation)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_write_diary, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            setMenuItemEnable(it)
            setMenuItemVisible(it)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    private fun setMenuItemEnable(menu: Menu) {
        menu.getItem(0)?.isEnabled =
            viewDataBinding.editTextTitle.text.isNotEmpty() && viewDataBinding.editTextText.text.isNotEmpty()
        menu.getItem(1)?.isEnabled = imageAdapter.selectedImageList.isNotEmpty()
    }

    private fun setMenuItemVisible(menu: Menu) {
        menu.getItem(0)?.isVisible = !isOpen
        menu.getItem(1)?.isVisible = isOpen
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> doOnBackPressed(!isOpen)
            R.id.menu_save -> {
                loadingDialog.show()
                viewModel.loadFile(imageAdapter.selectedImageList, this)
                viewModel.writeDiary()
            }
            R.id.menu_clear -> {
                imageAdapter.clearValue()
                invalidateOptionsMenu()
                title = "Gallery Image"
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun doOnBackPressed(isExit: Boolean) = if (isExit) dialog.show() else hideImageList()

    private fun setHomeDrawable() {
        if (isOpen) {
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        } else {
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        }
    }

    override fun onBackPressed() {
        doOnBackPressed(!isOpen)
    }


    private fun initActionBar() {
        title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHomeDrawable()
    }

    private fun initRecyclerView() {
        viewDataBinding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            setHasFixedSize(true)
            adapter = imageAdapter
        }
    }

    private fun initViewPager() {
        viewDataBinding.viewPager.apply {
            adapter = diaryWriteImageAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            viewDataBinding.wormDotsIndicator.setViewPager2(viewDataBinding.viewPager)
        }
    }


    private fun initDialog(dialogMsg: String) {
        val customDialog = layoutInflater.inflate(R.layout.custom_dialog, null)
        customDialogBinding =
            CustomDialogBinding.inflate(layoutInflater, customDialog as ViewGroup, false)
        dialog = Dialog(this)
        dialog.setContentView(customDialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        customDialogBinding.textView.text = dialogMsg
        customDialogBinding.textOk.setOnClickListener {
            finish()
        }
        customDialogBinding.textCancel.setOnClickListener {
            dialog.dismiss()
        }
    }
}
