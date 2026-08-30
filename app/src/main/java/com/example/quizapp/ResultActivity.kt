package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val score = intent.getIntExtra("score", 0)
        val total = intent.getIntExtra("total", 0)
        val categoryId = intent.getIntExtra("category_id", -1)
        val username = intent.getStringExtra("username") ?: ""

        val tvPlayer = findViewById<TextView>(R.id.tvPlayer)
        val tvScore = findViewById<TextView>(R.id.tvScore)
        val tvScoreCaption = findViewById<TextView>(R.id.tvScoreCaption)
        val scoreRing =
            findViewById<com.google.android.material.progressindicator.CircularProgressIndicator>(R.id.scoreRing)
        val btnBack = findViewById<Button>(R.id.btnBackToCategories)
        val btnReplay = findViewById<Button>(R.id.btnReplay)

        tvPlayer.text = "Gracz: $username"
        tvScore.text = "$score/$total"
        tvScoreCaption.text = when {
            score == 1 -> "POPRAWNA"
            score in 2..4 -> "POPRAWNE"
            else -> "POPRAWNYCH"
        }
        scoreRing.progress = if (total > 0) score * 100 / total else 0

        btnBack.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("username", username)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        }

        btnReplay.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("category_id", categoryId)
            intent.putExtra("username", username)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }

        val client = ApiClient.client
        val btnSave = findViewById<Button>(R.id.btnSaveScore)
        btnSave.setOnClickListener {

            val formBody = FormBody.Builder()
                .add("username", username)
                .add("category_id", categoryId.toString())
                .add("score", score.toString())
                .add("total", total.toString())
                .build()

            val request = Request.Builder()
                .url(ApiClient.BASE_URL + "save_score.php")
                .post(formBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        AppToast.show(this@ResultActivity, "Błąd zapisu")
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread {
                        AppToast.show(this@ResultActivity, "Wynik zapisany")
                    }
                }
            })
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}