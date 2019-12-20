package com.example.everyday_diary.utils

import android.view.animation.Animation

class CustomAnimationListener(val doOnAnimationEnd : () -> Unit) : Animation.AnimationListener {
    override fun onAnimationRepeat(animation: Animation?) {

    }

    override fun onAnimationEnd(animation: Animation?) {
        doOnAnimationEnd()
    }

    override fun onAnimationStart(animation: Animation?) {

    }
}