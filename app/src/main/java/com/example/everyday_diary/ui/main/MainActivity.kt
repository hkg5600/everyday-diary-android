package com.example.everyday_diary.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
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
import com.example.everyday_diary.network.response.MonthCount

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {
    override val layoutResourceId = R.layout.activity_main
    override val viewModel: MainActivityViewModel by viewModel()
    private var permission: Boolean = false
    private val monthAdapter: MonthAdapter by inject()

    override fun initView() {
        checkPermission()
        title = ""
        setSupportActionBar(toolbar)
        initNavigation()
        initViewPager()
        initMonthView()
        initDateTime()
    }

    override fun initObserver() {
        viewModel.data.observe(this, Observer {
            when (it) {
                is MonthCount -> {
                    monthAdapter.monthList.forEach { data ->
                        data.currentProgress = it.count[data.month - 1]
                        data.progress = "${it.count[data.month - 1]}/${data.total}"
                    }
                }
            }
        })
    }

    override fun initListener() {
        monthAdapter.onItemClickListener = object : MonthAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int, holder: MonthAdapter.MonthHolder) {
                startActivity(
                    Intent(this@MainActivity, DiaryListActivity::class.java)
                        .putExtra(
                            "month",
                            DateTimeConverter.monthToString(monthAdapter.monthList[position].month)
                        )
                        .putExtra("year", viewDataBinding.textViewYear.text)
                )
            }

        }
    }

    override fun initViewModel() {
        viewModel.getDiaryCount(Integer.parseInt(viewDataBinding.textViewYear.text.toString()))
    }

    @SuppressLint("SetTextI18n")
    private fun initDateTime() {
        val today: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e("Time:", "up sdk 24")
            viewDataBinding.textViewDate.text =
                "${DateTimeConverter.monthToString(LocalDate.now().month.value)}, ${LocalDate.now().dayOfMonth}/${LocalDateTime.now().year}"
            today = LocalDate.now().month.value
        } else {
            Log.e("Time:", "down sdk 24")
            viewDataBinding.textViewDate.text =
                "${DateTimeConverter.monthToString(Calendar.getInstance().time.month)}, ${Calendar.getInstance().time.day}/${Calendar.getInstance().time.year}"
            today = Calendar.getInstance().time.month
        }
        viewDataBinding.viewPager.setCurrentItem(today - 1, true)
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
        monthData.add(MonthAdapter.Month(1, "Jan", "0/31", 0, 31, "#b9fd75"))
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
        monthData.add(MonthAdapter.Month(12, "Dec", "0/31", 0, 31, "#ff4081"))
        monthAdapter.setMonthList(monthData)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_nav -> {
                viewDataBinding.drawerLayout.openDrawer(GravityCompat.END)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() =
        if (viewDataBinding.drawerLayout.isDrawerOpen(GravityCompat.END)) viewDataBinding.drawerLayout.closeDrawer(
            GravityCompat.END
        ) else super.onBackPressed()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun initNavigation() {
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            viewDataBinding.drawerLayout,
            viewDataBinding.appbarLayout.toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        viewDataBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        viewDataBinding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        viewDataBinding.drawerLayout.setDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {

            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerClosed(drawerView: View) {
                viewDataBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            override fun onDrawerOpened(drawerView: View) {
                viewDataBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

        })
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                (this),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            tedPermission()

        } else {
            permission = true
        }
    }

    private fun tedPermission() {

        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                permission = true
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                permission = false
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
}