package com.example.everyday_diary.base

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.everyday_diary.ui.start.StartActivity
import androidx.lifecycle.Observer
import com.example.everyday_diary.databinding.LoadingDialogBinding

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : Fragment() {

    lateinit var viewDataBinding: T

    abstract val layoutResourceId: Int

    abstract val viewModel: V

    abstract fun initView()

    abstract fun initObserver()

    abstract fun initListener()

    abstract fun initViewModel()

    private var isSetBackButtonValid = false

    lateinit var loadingDialog: Dialog

    lateinit var loadingDialogBinding: LoadingDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewDataBinding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        viewDataBinding.executePendingBindings()
        viewModel.networkError.observe(viewLifecycleOwner, Observer {
            makeToast("check your network connection", false)
            activity?.recreate()
        })

        viewModel.tokenChanged.observe(viewLifecycleOwner, Observer {
            if (it) {
                activity?.recreate()
            } else {
                makeToast("please sign in", true)
                startActivity(Intent(activity?.applicationContext, StartActivity::class.java))
                activity?.finish()
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it)
                loadingDialog.show()
            else
                loadingDialog.dismiss()
        })

        //BaseFragment
        initLoading()
        initView()
        initObserver()
        initListener()
        initViewModel()

        return viewDataBinding.root
    }

    private fun initLoading() {
        val loading = layoutInflater.inflate(com.example.everyday_diary.R.layout.loading_dialog, null)
        loadingDialogBinding = LoadingDialogBinding.inflate(layoutInflater, loading as ViewGroup, false)
        loadingDialog = Dialog(context!!)
        loadingDialog.setContentView(loadingDialogBinding.root)
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loadingDialog.setCancelable(false)
    }

    fun makeToast(msg: String, isLong : Boolean) {
        if (isLong)
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        else
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}