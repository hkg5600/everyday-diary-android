package com.example.travelercommunityapp.base

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.travelercommunityapp.utils.CustomDialog
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : Fragment() {

    lateinit var viewDataBinding: T

    abstract val layoutResourceId: Int

    abstract val viewModel: V

    abstract fun initView()

    abstract fun initObserver()

    abstract fun initListener()

    abstract fun initViewModel()

    private var isSetBackButtonValid = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewDataBinding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        viewDataBinding.executePendingBindings()

//BaseFragment
        initView()
        initObserver()
        initListener()
        initViewModel()

        return viewDataBinding.root
    }

    fun makeToast(msg: String, isLong : Boolean) {
        if (isLong)
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        else
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showDialog(msg: String, ok: () -> Unit ,cancel: () -> Unit) {
        CustomDialog(context!!).run {
            Builder()
                .setView(LayoutInflater.from(context).inflate(com.example.travelercommunityapp.R.layout.custom_dialog, null))
                .setMessage(msg)
                .show()
            Listener()
                .setOkayButton(View.OnClickListener {
                    ok()
                    this.dismiss()
                })
                .setCancelButton(View.OnClickListener {
                    cancel()
                    this.dismiss()
                })
        }
    }

    fun getSnackBar(viewBelow : View ,view: View, msg: String, clickMsg: String, onClick: () -> Unit) =
        Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE).apply {
            val snackBarView = this.view
            val params = snackBarView.layoutParams as CoordinatorLayout.LayoutParams
            params.anchorId = viewBelow.id
            params.anchorGravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            snackBarView.layoutParams = params
            setAction(clickMsg) {
                onClick()
                dismiss()
            }
        }
}