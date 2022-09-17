package com.r42914lg.arkados.yatodo.animation

import android.view.View
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.RotateAnimation

class ShakeAnimator(private val view: View) {
    private val rotate = RotateAnimation(-5F, 5F,
        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)

    init {
        rotate.duration = 250;
        rotate.startOffset = 50;
        rotate.repeatMode = Animation.REVERSE;
        rotate.interpolator = CycleInterpolator(5F);
    }

    fun start() { rotate.start() }
    fun stop() { rotate.cancel() }
}