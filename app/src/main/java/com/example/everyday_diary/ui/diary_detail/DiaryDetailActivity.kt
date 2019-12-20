    package com.example.everyday_diary.ui.diary_detail

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.transition.TransitionInflater
import android.view.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.example.everyday_diary.R
import com.example.everyday_diary.adapter.DiaryDetailImageAdapter
import com.example.everyday_diary.base.BaseActivity
import com.example.everyday_diary.databinding.ActivityDiaryDetailBinding
import com.example.everyday_diary.network.response.DiaryDetailReponse
import com.example.everyday_diary.utils.DateTimeConverter
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.app_bar.toolbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DiaryDetailActivity :
    BaseActivity<ActivityDiaryDetailBinding, DiaryDetailActivityViewModel>() {

    override val layoutResourceId = R.layout.activity_diary_detail
    override val viewModel: DiaryDetailActivityViewModel by viewModel()
    private val imageAdapter: DiaryDetailImageAdapter by inject()

    override fun initView() {
        window.enterTransition = TransitionInflater.from(this).inflateTransition(R.transition.explode)
        initActionBar()
        initViewPager()
    }

    @SuppressLint("SetTextI18n")
    override fun initObserver() {
        viewModel.data.observe(this, Observer {
            when (it) {
                is DiaryDetailReponse -> {
                    if (it.diary.images.isNotEmpty())
                        imageAdapter.setImageList(it.diary.images)
                    else
                        viewDataBinding.imageHolder.visibility = View.GONE
                    viewDataBinding.textViewDate.text =
                        "${DateTimeConverter.monthToString(it.diary.month)} " +
                                DateTimeConverter.dateTimeToDay(it.diary.created_at) +
                                "/${it.diary.year}"
                    viewDataBinding.textViewTitle.text = it.diary.title
                    viewDataBinding.textViewText.text = it.diary.text
                }
            }
        })
    }

    override fun initListener() {

    }

    override fun initViewModel() {
        getIntExtras()?.let {
            viewModel.getDiaryDetail(it)
        } ?: run {
            makeToast("try it again later", false)
            finishAfterTransition()
        }
    }

    private fun getIntExtras(): Int? = if (intent.getIntExtra("id", -1) == -1)
        null
    else
        intent.getIntExtra("id", -1)


    private fun initViewPager() {
        viewDataBinding.viewPager.apply {
            adapter = imageAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            viewDataBinding.wormDotsIndicator.setViewPager2(this)
        }
    }

    private fun initActionBar() {
        title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finishAfterTransition()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?) = true

    override fun onBackPressed() {
        finishAfterTransition()
    }
}
