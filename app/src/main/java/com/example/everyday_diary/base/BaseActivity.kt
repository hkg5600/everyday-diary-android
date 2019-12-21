package com.example.everyday_diary.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.everyday_diary.databinding.LoadingDialogBinding
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

    lateinit var loadingDialog: Dialog

    lateinit var loadingDialogBinding: LoadingDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        initBinding()
        initLoading()
        initView()
        initObserver()
        initListener()
        initViewModel()
        initBaseObserver()

    }

    private fun initBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, layoutResourceId)
        viewDataBinding.executePendingBindings()
    }

    private fun initBaseObserver() {
        viewModel.networkError.observe(this, Observer {
            makeToast("check your network connection", false)
            finish()
        })

        viewModel.tokenChanged.observe(this, Observer {
            if (it) {
                startActivity(
                    (Intent(
                        this,
                        this::class.java
                    )).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            } else {
                makeToast("please sign in", true)
                startActivity(Intent(this, StartActivity::class.java))
                finish()
            }
        })

        viewModel.loading.observe(this, Observer {
            if (it)
                loadingDialog.show()
            else
                loadingDialog.dismiss()
        })
    }

    private fun initLoading() {
        val loading =
            layoutInflater.inflate(com.example.everyday_diary.R.layout.loading_dialog, null)
        loadingDialogBinding =
            LoadingDialogBinding.inflate(layoutInflater, loading as ViewGroup, false)
        loadingDialog = Dialog(this)
        loadingDialog.setContentView(loadingDialogBinding.root)
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loadingDialog.setCancelable(false)
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