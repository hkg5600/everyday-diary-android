package com.example.everyday_diary.ui.diary_list

import android.view.MenuItem
import androidx.lifecycle.Observer
import com.example.everyday_diary.R
import com.example.everyday_diary.adapter.DiaryListAdapter
import com.example.everyday_diary.base.BaseActivity
import com.example.everyday_diary.databinding.ActivityDiaryListBinding
import com.example.everyday_diary.network.response.DiaryListResponse
import com.example.everyday_diary.utils.DateTimeConverter
import kotlinx.android.synthetic.main.app_bar.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DiaryListActivity : BaseActivity<ActivityDiaryListBinding, DiaryListActivityViewModel>() {

    override val layoutResourceId = R.layout.activity_diary_list
    override val viewModel: DiaryListActivityViewModel by viewModel()

    private val diaryListAdapter: DiaryListAdapter by inject()

    override fun initView() {
        initActionBar()
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

    }

    override fun initViewModel() {
        val (month, year) = getExtras()
        setTitle(month, year)
        viewModel.getDiaryByDate(DateTimeConverter.stringToMonth(month), year)
    }

    private fun getExtras(): Pair<String, Int> {
        val month = intent.getStringExtra("month")
        val year = intent.getIntExtra("year", -1)
        if (month == null || year == -1)
            finish()
        return Pair(month, year)
    }

    private fun setTitle(month: String, year: Int) {
        title = "$month / $year"
    }

    private fun initActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
