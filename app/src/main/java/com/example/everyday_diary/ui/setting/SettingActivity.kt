package com.example.everyday_diary.ui.setting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.view.MenuItem
import androidx.core.app.NotificationCompat
import com.example.everyday_diary.R
import com.example.everyday_diary.base.BaseActivity
import com.example.everyday_diary.databinding.ActivitySettingBinding
import com.example.everyday_diary.ui.main.MainActivity
import kotlinx.android.synthetic.main.app_bar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingActivity : BaseActivity<ActivitySettingBinding, SettingActivityViewModel>() {
    override val layoutResourceId = R.layout.activity_setting
    override val viewModel: SettingActivityViewModel by viewModel()
    private lateinit var settings: SharedPreferences

    override fun initView() {
        settings = getSharedPreferences("Alarm", Context.MODE_PRIVATE)
        initActionBar()
        initSwitch()
    }

    override fun initObserver() {

    }

    override fun initListener() {

    }

    override fun initViewModel() {

    }

    private fun makeNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.putExtra("notificationId", 100)
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, "alarm")
            .setContentTitle("everyday diary")
            .setContentText("Write your today")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            builder.setSmallIcon(R.drawable.ic_launcher_foreground) //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            val channelName = "notification channel"
            val description = "for upper oreo"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel("alarm", channelName, importance)
            channel.description = description

            notificationManager.createNotificationChannel(channel)

        } else builder.setSmallIcon(R.mipmap.ic_launcher)

        notificationManager.notify(1234, builder.build()) // 고유숫자로 노티피케이션 동작시킴

    }


    private fun initSwitch() {
        viewDataBinding.switchButton.isChecked = settings.getBoolean("switch_key", false)
        viewDataBinding.switchButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

            } else {

            }
            settings.edit().putBoolean("switch_key", isChecked).apply()
        }
    }

    private fun initActionBar() {
        title = "Setting"
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

}
