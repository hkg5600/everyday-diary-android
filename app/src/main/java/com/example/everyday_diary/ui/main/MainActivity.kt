package com.example.everyday_diary.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.app_bar.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import androidx.lifecycle.Observer
import com.example.everyday_diary.databinding.MainBottomSheetDialogBinding
import com.example.everyday_diary.databinding.YearPickerBinding
import com.example.everyday_diary.network.response.MonthCount
import com.example.everyday_diary.network.response.UserInfoResponse
import com.example.everyday_diary.ui.start.StartActivity
import com.example.everyday_diary.ui.write_activity.WriteDiaryActivity
import com.example.everyday_diary.utils.UserObject
import com.google.android.material.bottomsheet.BottomSheetDialog

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
    }

    override fun initViewModel() {
        viewModel.getUserInfo()
        viewModel.getDiaryCount(Integer.parseInt(viewDataBinding.textViewYear.text.toString()))
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
        monthData.add(MonthAdapter.Month(1, "Jan", "0/31", 0, 31, "#90cbc4"))
        monthData.add(MonthAdapter.Month(2, "Feb", "0/29", 0, 29, "#00fff4"))
        monthData.add(MonthAdapter.Month(3, "Mar", "0/31", 0, 31, "#4343f6"))
        monthData.add(MonthAdapter.Month(4, "Apr", "0/30", 0, 30, "#ca5ff4"))
        monthData.add(MonthAdapter.Month(5, "May", "0/31", 0, 31, "#f887ff"))
        monthData.add(MonthAdapter.Month(6, "Jun", "0/30", 0, 30, "#0d97a7"))
        monthData.add(MonthAdapter.Month(7, "Jul", "0/31", 0, 31, "#92cbc5"))
        monthData.add(MonthAdapter.Month(8, "Aug", "0/31", 0, 31, "#3f51b5"))
        monthData.add(MonthAdapter.Month(9, "Sep", "0/30", 0, 30, "#ffb8ea"))
        monthData.add(MonthAdapter.Month(10, "Oct", "0/31", 0, 31, "#b92d02"))
        monthData.add(MonthAdapter.Month(11, "Nov", "0/30", 0, 30, "#dfaeff"))
        monthData.add(MonthAdapter.Month(12, "Dec", "0/31", 0, 31, "#ff6e6e"))
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
            viewDataBinding.textViewYear.text = yearPickerBinding.yearPicker.value.toString()
            viewModel.getDiaryCount(Integer.parseInt(viewDataBinding.textViewYear.text.toString()))
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
    }
}