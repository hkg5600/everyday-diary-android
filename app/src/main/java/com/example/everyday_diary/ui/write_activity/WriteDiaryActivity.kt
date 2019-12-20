package com.example.everyday_diary.ui.write_activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
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
import com.example.everyday_diary.utils.CustomAnimationListener
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
        setDate()
        viewDataBinding.activity = this
        viewDataBinding.vm = viewModel
    }

    override fun initObserver() {
        imageAdapter.overSize.observe(this, Observer {
            makeToast("You can select up to 13", false)
        })

        imageAdapter.onChanged.observe(this, Observer {
            setTitle(if (imageAdapter.selectedImageList.isEmpty()) "Gallery Image" else "${imageAdapter.selectedImageList.size} selected")
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


    private fun setDate() {
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

    private fun setTitle(titleText: String) {
        title = titleText
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
        setTitle(if (imageAdapter.selectedImageList.isNotEmpty()) "${imageAdapter.selectedImageList.size} selected" else "Gallery Image")
        hideKeyboard()
        isOpen = true
        invalidateOptionsMenu()
        setHomeDrawable()
        viewDataBinding.galleryImageHolder.run {
            visibility = View.VISIBLE
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.gallery_image_show).also {
                it.setAnimationListener(CustomAnimationListener {
                    viewDataBinding.scrollView.visibility = View.GONE
                })
            })
        }
        viewDataBinding.fabClose.run {
            visibility = View.VISIBLE
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.gallery_image_show))
        }
    }

    private fun hideImageList() {
        setTitle("")
        imageAdapter.isClosing = true
        isOpen = false
        invalidateOptionsMenu()
        setHomeDrawable()
        viewDataBinding.fabClose.visibility = View.GONE
        viewDataBinding.scrollView.visibility = View.VISIBLE
        viewDataBinding.galleryImageHolder.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.gallery_image_hide).also {
                it.setAnimationListener(CustomAnimationListener {
                    viewDataBinding.galleryImageHolder.visibility = View.GONE
                    imageAdapter.isClosing = false
                })
            })
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

    private fun setHomeDrawable() {
        if (isOpen) {
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        } else {
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        }
    }

    private fun doOnBackPressed(isExit: Boolean) = if (isExit) dialog.show() else hideImageList()

    private fun initActionBar() {
        setTitle("")
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
            viewDataBinding.wormDotsIndicator.setViewPager2(this)
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
                setTitle("Gallery Image")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        doOnBackPressed(!isOpen)
    }
}
