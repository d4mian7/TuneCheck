package com.example.quizapp

import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView

/**
 * Komunikaty w motywie aplikacji (zamiast systemowego Toasta).
 * Nakładka rysowana na oknie aktywności, z animacją pojawienia i zniknięcia.
 */
object AppToast {

    fun show(activity: Activity, message: String) {
        if (activity.isFinishing || activity.isDestroyed) return
        show(activity, activity.window.decorView as ViewGroup, 32, message)
    }

    private fun show(activity: Activity, decor: ViewGroup, bottomDp: Int, message: String) {
        val view = LayoutInflater.from(activity).inflate(R.layout.view_app_toast, decor, false)
        view.findViewById<TextView>(R.id.tvToastText).text = message

        val density = activity.resources.displayMetrics.density
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        params.bottomMargin = (bottomDp * density).toInt()
        view.layoutParams = params

        view.alpha = 0f
        view.translationY = 20 * density
        decor.addView(view)
        view.animate().alpha(1f).translationY(0f).setDuration(180).start()

        view.postDelayed({
            view.animate().alpha(0f).translationY(12 * density).setDuration(220)
                .withEndAction { decor.removeView(view) }
                .start()
        }, 2200)
    }
}
