package com.canvascue.ui.transitions

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import androidx.transition.Transition
import androidx.transition.TransitionValues

class FadeScaleTransition : Transition() {
    private val PROP_ALPHA = "canvascue:fadescale:alpha"
    private val PROP_SCALE_X = "canvascue:fadescale:scaleX"
    private val PROP_SCALE_Y = "canvascue:fadescale:scaleY"

    override fun captureStartValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    private fun captureValues(transitionValues: TransitionValues) {
        val view = transitionValues.view
        transitionValues.values[PROP_ALPHA] = view.alpha
        transitionValues.values[PROP_SCALE_X] = view.scaleX
        transitionValues.values[PROP_SCALE_Y] = view.scaleY
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        if (startValues == null || endValues == null) return null

        val view = endValues.view
        view.alpha = 0f
        view.scaleX = 0.8f
        view.scaleY = 0.8f

        return ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).apply {
            duration = 300
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    view.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(duration)
                        .start()
                }
                override fun onAnimationEnd(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
        }
    }
}