package com.example.everyday_diary.ui.setting

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.MenuItem
import com.example.everyday_diary.R
import com.example.everyday_diary.base.BaseActivity
import com.example.everyday_diary.databinding.ActivitySettingBinding
import com.example.everyday_diary.utils.AlarmReceiver
import kotlinx.android.synthetic.main.app_bar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class SettingActivity : BaseActivity<ActivitySettingBinding, SettingActivityViewModel>() {
    override val layoutResourceId = R.layout.activity_setting
    override val viewModel: SettingActivityViewModel by viewModel()
    private lateinit var settings: SharedPreferences
    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent
    lateinit var dialog: TimePickerDialog
    lateinit var dialogListener: TimePickerDialog.OnTimeSetListener
    private var calendar = Calendar.getInstance()
    private var hour = 0
    private var minute = 0

    override fun initView() {
        settings = getSharedPreferences("Alarm", Context.MODE_PRIVATE)
        setSavedTime()
        initActionBar()
        initAlarmText()
        initAlarm()
        initTimePickerListener()
        initSwitch()
        setCalendar(hour, minute)
        viewDataBinding.activity = this
    }

    override fun initObserver() {

    }

    override fun initListener() {

    }

    override fun initViewModel() {

    }

    private fun initAlarm() {
        val intent = Intent(this, AlarmReceiver::class.java)
        pendingIntent =
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    private fun setSavedTime() {
        hour = settings.getInt("hour", 12)
        minute = settings.getInt("minute", 12)
    }

    private fun initSwitch() {
        viewDataBinding.switchButton.isChecked = settings.getBoolean("switch_key", false)
        viewDataBinding.switchButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setAlarm()
            } else {
                alarmManager.cancel(pendingIntent)
            }
            settings.edit().putBoolean("switch_key", isChecked).apply()
        }
    }

    fun showDialog() {
        dialog = TimePickerDialog(this, dialogListener, 0, 0, true)
        dialog.show()
    }

    private fun initTimePickerListener() {
        dialogListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            settings.edit().putInt("hour", hourOfDay).putInt("minute", minute).apply()
            setCalendar(hourOfDay, minute)
            setAlarmText(hourOfDay, minute)
            viewDataBinding.switchButton.isChecked = true
            setAlarm()
            dialog.dismiss()
        }
    }


    private fun setCalendar(hour: Int, minute: Int) {
        calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        if (calendar.timeInMillis < Calendar.getInstance().timeInMillis)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    private fun initAlarmText() {
        setAlarmText(hour, minute)
    }

    @SuppressLint("SetTextI18n")
    private fun setAlarmText(hour: Int, minute: Int) {
        viewDataBinding.textViewTime.text = "$hour:$minute"
    }

    private fun setAlarm() {
        Log.e("Time1", Calendar.getInstance().timeInMillis.toString())
        Log.e("Time2", calendar.timeInMillis.toString())
        Log.e(
            "Time sub",
            ((calendar.timeInMillis - Calendar.getInstance().timeInMillis) / 1000).toString()
        )
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            60000,
            pendingIntent
        )
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
