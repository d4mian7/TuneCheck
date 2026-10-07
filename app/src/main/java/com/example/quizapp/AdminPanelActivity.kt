package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

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
            logout()
            MainActivity.showLogoutMessage = true
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    // wylogowanie: serwer kasuje token (stary przestaje działać), aplikacja go zapomina;
    // odpowiedź nie jest potrzebna, więc callback jest pusty
    private fun logout() {
        val request = Request.Builder()
            .url(ApiClient.BASE_URL + "admin_logout.php")
            .header("X-Admin-Token", ApiClient.adminToken ?: "")
            .post(FormBody.Builder().build())
            .build()
        ApiClient.client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) { response.close() }
        })
        ApiClient.adminToken = null
    }
}