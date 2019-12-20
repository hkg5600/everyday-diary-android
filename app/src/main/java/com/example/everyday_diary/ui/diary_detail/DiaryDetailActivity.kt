package com.example.everyday_diary.ui.diary_detail

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import androidx.viewpager2.widget.ViewPager2
import com.example.everyday_diary.R
import com.example.everyday_diary.adapter.DiaryWriteImageAdapter
import com.example.everyday_diary.base.BaseActivity
import com.example.everyday_diary.databinding.ActivityDiaryDetailBinding
import kotlinx.android.synthetic.main.app_bar.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DiaryDetailActivity :
    BaseActivity<ActivityDiaryDetailBinding, DiaryDetailActivityViewModel>() {

    override val layoutResourceId = R.layout.activity_diary_detail
    override val viewModel: DiaryDetailActivityViewModel by viewModel()
    private val imageAdapter : DiaryWriteImageAdapter by inject()

    override fun initView() {
        initActionBar()
        initViewPater()
    }

    override fun initObserver() {

    }

    override fun initListener() {

    }

    override fun initViewModel() {

    }

    private fun initViewPater() {
        viewDataBinding.viewPager.apply {
            adapter = imageAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            viewDataBinding.wormDotsIndicator.setViewPager2(viewDataBinding.viewPager)
        }
    }

    private fun initActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> finishAfterTransition()
        }
        return super.onOptionsItemSelected(item)
    }

}
