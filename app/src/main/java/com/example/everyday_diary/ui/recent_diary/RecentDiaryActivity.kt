package com.example.everyday_diary.ui.recent_diary

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.example.everyday_diary.R
import com.example.everyday_diary.adapter.DiaryListAdapter
import com.example.everyday_diary.base.BaseActivity
import com.example.everyday_diary.databinding.ActivityRecentDiaryBinding
import com.example.everyday_diary.network.response.DiaryListResponse
import com.example.everyday_diary.ui.diary_detail.DiaryDetailActivity
import kotlinx.android.synthetic.main.app_bar.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecentDiaryActivity :
    BaseActivity<ActivityRecentDiaryBinding, RecentDiaryActivityViewModel>() {
    override val layoutResourceId = R.layout.activity_recent_diary
    override val viewModel: RecentDiaryActivityViewModel by viewModel()

    private val diaryListAdapter: DiaryListAdapter by inject()

    override fun initView() {
        initActionBar()
        initRecyclerView()
    }

    override fun initObserver() {
        viewModel.data.observe(this, Observer {
            when (it) {
                is DiaryListResponse -> {
                    diaryListAdapter.setDiaryList(it.diary)
                }
            }
        })
    }

    override fun initListener() {
        diaryListAdapter.onItemClickListener = object : DiaryListAdapter.OnItemClickListener {
            override fun onClick(
                view: View,
                position: Int,
                holder: DiaryListAdapter.DiaryListHolder
            ) {
                startActivity(
                    Intent(
                        this@RecentDiaryActivity,
                        DiaryDetailActivity::class.java
                    ).putExtra("id", diaryListAdapter.diaryList[position].id),
                    ActivityOptions.makeSceneTransitionAnimation(this@RecentDiaryActivity).toBundle()
                )
            }
        }
    }

    override fun initViewModel() {
        viewModel.getRecentDiary()
    }

    private fun initActionBar() {
        title = "Recent"
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initRecyclerView() {
        viewDataBinding.recyclerView.apply {
            setHasFixedSize(true)
            adapter = diaryListAdapter
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
