package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AdminPanelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        if (savedInstanceState == null) {
            window.decorView.postDelayed({ AppToast.show(this, "Zalogowano jako admin") }, 500)
        }

        val btnManageCategories = findViewById<Button>(R.id.btnManageCategories)
        val btnManageQuestions = findViewById<Button>(R.id.btnManageQuestions)
        val btnBack = findViewById<Button>(R.id.btnAdminBack)

        btnManageCategories.setOnClickListener {
            startActivity(Intent(this, AdminCategoriesActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btnManageQuestions.setOnClickListener {
            startActivity(Intent(this, AdminQuestionsActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btnBack.setOnClickListener {
            MainActivity.showLogoutMessage = true
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }
}