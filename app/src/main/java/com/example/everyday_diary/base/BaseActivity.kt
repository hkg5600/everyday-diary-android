package com.example.everyday_diary.base


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.everyday_diary.ui.start.StartActivity


abstract class BaseActivity<T : ViewDataBinding, R : BaseViewModel> : AppCompatActivity() {

    lateinit var viewDataBinding: T

    abstract val layoutResourceId: Int

    abstract val viewModel: R

    abstract fun initView()

    abstract fun initObserver()

    abstract fun initListener()

    abstract fun initViewModel()

    private var isSetBackButtonValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        viewDataBinding = DataBindingUtil.setContentView(this, layoutResourceId)
        viewDataBinding.executePendingBindings()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        initView()
        initObserver()
        initListener()
        initViewModel()

        viewModel.networkError.observe(this, Observer {
            makeToast("네트워크 연결 상태를 확인해 주세요", false)
            startActivity((Intent(this, this::class.java)).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
        })

        viewModel.tokenChanged.observe(this, Observer {
            if (it) {
                startActivity((Intent(this, this::class.java)).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
            } else {
                makeToast("refresh token", true)
                startActivity(Intent(this, StartActivity::class.java))
                finish()
            }
        })
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(if (currentFocus == null) View(this) else currentFocus!!)
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Fragment.showKeyboard() {
        view?.let { activity?.showKeyboard(it) }
    }

    fun Activity.showKeyboard() {
        showKeyboard(if (currentFocus == null) View(this) else currentFocus!!)
    }

    private fun Context.showKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, 0)
    }

    fun makeToast(msg: String, isLong: Boolean) {
        if (isLong)
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        else
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}