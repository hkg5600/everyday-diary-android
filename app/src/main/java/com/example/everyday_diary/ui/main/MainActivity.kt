package com.example.everyday_diary.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.example.everyday_diary.R
import com.example.everyday_diary.adapter.MonthAdapter
import com.example.everyday_diary.base.BaseActivity
import com.example.everyday_diary.databinding.ActivityMainBinding
import com.example.everyday_diary.ui.diary_list.DiaryListActivity
import com.example.everyday_diary.utils.DateTimeConverter
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.everyday_diary.adapter.GalleryImageAdapter
import com.example.everyday_diary.databinding.MainBottomSheetDialogBinding
import com.example.everyday_diary.databinding.YearPickerBinding
import com.example.everyday_diary.network.response.CardImageResponse
import com.example.everyday_diary.network.response.MonthCount
import com.example.everyday_diary.network.response.UserInfoResponse
import com.example.everyday_diary.ui.start.StartActivity
import com.example.everyday_diary.ui.write_activity.WriteDiaryActivity
import com.example.everyday_diary.utils.FileUtil
import com.example.everyday_diary.utils.UserObject
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {
    override val layoutResourceId = R.layout.activity_main
    override val viewModel: MainActivityViewModel by viewModel()
    private val monthAdapter: MonthAdapter by inject()
    private var monthOfToday = 0
    private lateinit var dialog: Dialog
    private lateinit var yearPickerBinding: YearPickerBinding
    private lateinit var bottomSheetBinding: MainBottomSheetDialogBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun initView() {
        viewDataBinding.buttonWrite.isEnabled = false
        checkPermission()
        initBottomSheetBinding()
        initBottomSheetDialog()
        initViewPager()
        initYearDialog()
        initYearPicker()
        setMonthOfToday()
        setToday()
        initMonthView()
        setViewPagerPos()
        bottomSheetClick()
        viewDataBinding.textViewYear.text = Calendar.getInstance().get(Calendar.YEAR).toString()
        viewDataBinding.activity = this
    }

    override fun initObserver() {
        viewModel.data.observe(this, Observer {
            when (it) {
                is MonthCount -> {
                    monthAdapter.monthList.forEach { data ->
                        data.currentProgress = it.count[data.month - 1]
                        data.progress = "${it.count[data.month - 1]}/${data.total}"
                        monthAdapter.notifyDataSetChanged()
                    }
                }

                is UserInfoResponse -> {
                    UserObject.user = it.user
                    viewDataBinding.buttonWrite.isEnabled = true
                }

                is CardImageResponse -> {
                    monthAdapter.selectedItem.clear()
                    setCardImage(it)
                    monthAdapter.notifyDataSetChanged()
                }
            }
        })

        viewModel.message.observe(this, Observer {
            makeToast(it, false)
            when (it) {
                "success to delete" -> {
                    viewModel.getCardImage(viewDataBinding.textViewYear.text.toString().toInt())
                }
                "success to add" -> {
                    viewModel.getCardImage(viewDataBinding.textViewYear.text.toString().toInt())
                }
            }
        })

        viewModel.roomSuccess.observe(this, Observer {
            when (it) {
                "logout" -> {
                    startActivity(Intent(this, StartActivity::class.java))
                    finish()
                }
            }
        })

        viewModel.roomError.observe(this, Observer {
            makeToast("Error: Try again later", false)
        })
    }

    override fun initListener() {
        monthAdapter.onItemClickListener = object : MonthAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int, holder: MonthAdapter.MonthHolder) {
                startActivityForResult(
                    Intent(this@MainActivity, DiaryListActivity::class.java)
                        .putExtra(
                            "month",
                            DateTimeConverter.monthToString(monthAdapter.monthList[position].month)
                        )
                        .putExtra("year", viewDataBinding.textViewYear.text), 1
                )
            }
        }

        monthAdapter.onMenuClickListener = object : MonthAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int, holder: MonthAdapter.MonthHolder) {
                val popupMenu = PopupMenu(this@MainActivity, view)
                menuInflater.inflate(R.menu.menu_month_card, popupMenu.menu)
                popupMenu.apply {
                    setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.menu_image_change -> {
                                pickFromGallery()
                            }
                            R.id.menu_image_default -> {
                                viewModel.deleteCardImage(monthAdapter.monthList[position].image?.id!!)
                            }
                        }
                        false
                    }
                    show()
                }
            }
        }
    }

    override fun initViewModel() {
        viewModel.getUserInfo()
        viewModel.getDiaryCount(Integer.parseInt(viewDataBinding.textViewYear.text.toString()))
        viewModel.getCardImage(viewDataBinding.textViewYear.text.toString().toInt())
    }

    private fun pickFromGallery() {
        startActivityForResult(Intent(Intent.ACTION_PICK).setType("image/*"), 2)
    }

    fun startWriteActivity() {
        startActivityForResult(
            Intent(this@MainActivity, WriteDiaryActivity::class.java)
                .putExtra("month", monthOfToday.toString())
                .putExtra("year", viewDataBinding.textViewYear.text), 1
        )
    }

    private fun bottomSheetClick() {
        bottomSheetBinding.textViewLogOut.setOnClickListener {
            viewModel.logout()
        }
        bottomSheetBinding.textViewRecent.setOnClickListener {
            makeToast("recent", false)
        }
        bottomSheetBinding.textViewSetting.setOnClickListener {
            makeToast("setting", false)
        }
    }

    private fun doOnFileExists(file: File) {
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val multipartData =
            MultipartBody.Part.createFormData("image", "file.jpg", requestFile)
        viewModel.postCardImage(
            monthAdapter.monthList[viewDataBinding.viewPager.currentItem].month.toString(),
            viewDataBinding.textViewYear.text.toString(),
            multipartData
        )
    }

    private fun setCardImage(cardResponse: CardImageResponse) {
        cardResponse.card_image.map { cardImage ->
            monthAdapter.monthList.filter { image ->
                image.month == cardImage.month
            }.map { card ->
                card.image = cardImage
                monthAdapter.selectedItem.put(cardImage.month - 1, true)
            }
        }
    }

    private fun setMonthOfToday() {
        monthOfToday = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getMonthWIthUpApi()
        } else {
            getMonthWIthUnderApi()
        }
    }

    private fun setViewPagerPos() {
        viewDataBinding.viewPager.setCurrentItem(monthOfToday - 1, true)
    }

    @SuppressLint("SetTextI18n")
    @TargetApi(Build.VERSION_CODES.O)
    private fun getMonthWIthUpApi() = LocalDate.now().month.value

    @SuppressLint("SetTextI18n")
    private fun getMonthWIthUnderApi() = Calendar.getInstance().time.month

    private fun setToday() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setTodayWithUpApi()
        } else {
            setTodayWIthUnderApi()
        }
    }

    @SuppressLint("NewApi", "SetTextI18n")
    @TargetApi(Build.VERSION_CODES.O)
    private fun setTodayWithUpApi() {
        viewDataBinding.textViewDate.text =
            "${DateTimeConverter.monthToString(LocalDate.now().month.value)}, ${LocalDate.now().dayOfMonth}/${LocalDateTime.now().year}"
    }

    @SuppressLint("SetTextI18n")
    private fun setTodayWIthUnderApi() {
        viewDataBinding.textViewDate.text =
            "${DateTimeConverter.monthToString(Calendar.getInstance().time.month)}, ${Calendar.getInstance().time.day}/${Calendar.getInstance().time.year}"
    }

    fun showYearPicker() {
        dialog.show()
    }

    fun showBottomSheet() {
        bottomSheetDialog.show()
    }


    private fun initViewPager() {
        viewDataBinding.viewPager.apply {
            adapter = monthAdapter
            orientation = ORIENTATION_HORIZONTAL
            offscreenPageLimit = 1
            val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
            val currentItemHorizontalMarginPx =
                resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
            val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
            val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
                page.translationX = -pageTranslationX * position
                page.scaleY = 1 - (0.15f * abs(position))
            }
            setPageTransformer(pageTransformer)
        }
    }

    private fun initMonthView() {
        val monthData = ArrayList<MonthAdapter.Month>()
        monthData.add(MonthAdapter.Month(1, "Jan", "0/31", 0, 31, "#90cbc4", null))
        monthData.add(MonthAdapter.Month(2, "Feb", "0/29", 0, 29, "#00fff4", null))
        monthData.add(MonthAdapter.Month(3, "Mar", "0/31", 0, 31, "#4343f6", null))
        monthData.add(MonthAdapter.Month(4, "Apr", "0/30", 0, 30, "#ca5ff4", null))
        monthData.add(MonthAdapter.Month(5, "May", "0/31", 0, 31, "#f887ff", null))
        monthData.add(MonthAdapter.Month(6, "Jun", "0/30", 0, 30, "#0d97a7", null))
        monthData.add(MonthAdapter.Month(7, "Jul", "0/31", 0, 31, "#92cbc5", null))
        monthData.add(MonthAdapter.Month(8, "Aug", "0/31", 0, 31, "#3f51b5", null))
        monthData.add(MonthAdapter.Month(9, "Sep", "0/30", 0, 30, "#ffb8ea", null))
        monthData.add(MonthAdapter.Month(10, "Oct", "0/31", 0, 31, "#b92d02", null))
        monthData.add(MonthAdapter.Month(11, "Nov", "0/30", 0, 30, "#dfaeff", null))
        monthData.add(MonthAdapter.Month(12, "Dec", "0/31", 0, 31, "#ff6e6e", null))
        monthAdapter.setMonthList(monthData)
    }

    private fun initBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
    }

    private fun initBottomSheetBinding() {
        val bottomSheetInflater = layoutInflater.inflate(R.layout.main_bottom_sheet_dialog, null)
        bottomSheetBinding = MainBottomSheetDialogBinding.inflate(
            layoutInflater,
            bottomSheetInflater as ViewGroup,
            false
        )
    }

    private fun initYearPicker() {
        yearPickerBinding.yearPicker.apply {
            minValue = 1980
            maxValue = Calendar.getInstance().get(Calendar.YEAR)
            value = Calendar.getInstance().get(Calendar.YEAR)
        }
        yearPickerBinding.textOk.setOnClickListener {
            initMonthView()
            viewDataBinding.textViewYear.run {
                text = yearPickerBinding.yearPicker.value.toString()
                viewModel.getDiaryCount(this.text.toString().toInt())
                viewModel.getCardImage(this.text.toString().toInt())
            }
            viewDataBinding.viewPager.setCurrentItem(0, true)
            dialog.dismiss()
        }
        yearPickerBinding.textCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun initYearDialog() {
        val customDialog = layoutInflater.inflate(R.layout.year_picker, null)
        yearPickerBinding =
            YearPickerBinding.inflate(layoutInflater, customDialog as ViewGroup, false)
        dialog = Dialog(this)
        dialog.setContentView(yearPickerBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                (this),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            tedPermission()
        }
    }

    private fun tedPermission() {

        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {

            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                finish()
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setRationaleMessage(getString(R.string.permissionMsg))
            .setDeniedMessage(getString(R.string.permissionDenied))
            .setPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.getDiaryCount(Integer.parseInt(viewDataBinding.textViewYear.text.toString()))
        viewModel.getCardImage(viewDataBinding.textViewYear.text.toString().toInt())

        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            val url = FileUtil.getRealPathFromURI(data?.data, this)
            val file = File(FileUtil.getRealPathFromURI(Uri.parse(url), applicationContext)!!)
            if (file.exists()) {
                doOnFileExists(file)
            }
        }
    }

}