package com.example.everyday_diary.ui.diary_list

import android.view.MenuItem
import com.example.everyday_diary.R
import com.example.everyday_diary.base.BaseActivity
import com.example.everyday_diary.databinding.ActivityDiaryListBinding
import kotlinx.android.synthetic.main.app_bar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DiaryListActivity : BaseActivity<ActivityDiaryListBinding, DIaryListActivitViewModel>() {

    override val layoutResourceId = R.layout.activity_diary_list
    override val viewModel: DIaryListActivitViewModel by viewModel()

    override fun initView() {
        initActionBar()
        setTitle()
    }

    override fun initObserver() {

    }

    override fun initListener() {

    }

    override fun initViewModel() {

    }

    private fun setTitle() {
        intent.extras?.get("month")?.let {month ->
            intent.extras?.get("year")?.let {year ->
                title = "$month / $year"
            }   
        }
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
