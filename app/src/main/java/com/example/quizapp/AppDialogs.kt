package com.example.quizapp

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView

/**
 * Dialogi w motywie aplikacji (zamiast systemowego AlertDialog).
 */
object AppDialogs {

    fun confirm(
        activity: Activity,
        title: String,
        message: String,
        onConfirm: () -> Unit
    ) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_confirm)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            (activity.resources.displayMetrics.widthPixels * 0.85).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.findViewById<TextView>(R.id.tvConfirmTitle).text = title
        dialog.findViewById<TextView>(R.id.tvConfirmMessage).text = message
        dialog.findViewById<TextView>(R.id.btnConfirmYes).setOnClickListener {
            dialog.dismiss()
            onConfirm()
        }
        dialog.findViewById<TextView>(R.id.btnConfirmNo).setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
}
