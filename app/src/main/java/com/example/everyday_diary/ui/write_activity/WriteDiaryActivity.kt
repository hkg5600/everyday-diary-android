package com.example.everyday_diary.ui.write_activity

import android.app.Dialog
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import com.example.everyday_diary.R
import com.example.everyday_diary.base.BaseActivity
import com.example.everyday_diary.databinding.ActivityWriteDiaryBinding
import com.example.everyday_diary.databinding.CustomDialogBinding
import kotlinx.android.synthetic.main.app_bar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class WriteDiaryActivity : BaseActivity<ActivityWriteDiaryBinding, WriteDiaryActivityViewModel>() {
    override val layoutResourceId = R.layout.activity_write_diary
    override val viewModel: WriteDiaryActivityViewModel by viewModel()

    override fun initView() {
        initActionBar()
        initDialog()
    }

    override fun initObserver() {

    }

    override fun initListener() {

    }

    override fun initViewModel() {

    }

    private fun initDialog() {
        val customDialog = layoutInflater.inflate(R.layout.custom_dialog, null)
        val customDialogBinding = CustomDialogBinding.inflate(layoutInflater, customDialog as ViewGroup, false)
        val dialog = Dialog(this)
        dialog.setContentView(customDialogBinding.root)
        customDialogBinding.textView.text = "TEST"
        dialog.show()
    }

    private fun initActionBar() {
        title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_write_diary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.menu_save -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

}
