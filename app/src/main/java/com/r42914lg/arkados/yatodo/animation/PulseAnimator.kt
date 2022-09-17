package com.r42914lg.arkados.yatodo.animation

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

class PulseAnimator(private val view: View) {
    private val scaleDown: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(view,
        PropertyValuesHolder.ofFloat("scaleX", 1.2f),
        PropertyValuesHolder.ofFloat("scaleY", 1.2f)
    )

    init {
        scaleDown.duration = 310
        scaleDown.repeatCount = ObjectAnimator.INFINITE
        scaleDown.repeatMode = ObjectAnimator.REVERSE
        scaleDown.interpolator = FastOutSlowInInterpolator()
    }

    fun start() { scaleDown.start() }
    fun stop() { scaleDown.cancel() }
}