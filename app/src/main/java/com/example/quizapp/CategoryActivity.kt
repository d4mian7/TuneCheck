package com.example.quizapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.util.Log
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import android.content.Intent
import android.graphics.Color

class CategoryActivity : AppCompatActivity() {

    private val client = ApiClient.client
    private val URL = "http://10.0.2.2/quiz_api/get_categories.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val username = intent.getStringExtra("username") ?: ""
        val layout = findViewById<LinearLayout>(R.id.layoutCategories)
        val contentLayout = findViewById<LinearLayout>(R.id.contentLayout)
        val loadingSpinner = findViewById<ProgressBar>(R.id.loadingSpinner)

        val request = Request.Builder().url(URL).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                Log.d("QUIZ", "JSON = $json")

                if (json == null) return

                val jsonArray = JSONArray(json)

                runOnUiThread {
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val id = obj.getInt("id")
                        val name = obj.getString("name")

                        val button = Button(this@CategoryActivity)
                        button.text = name
                        button.setBackgroundResource(R.drawable.category_tile)
                        button.setTextColor(Color.parseColor("#F5E6C4"))
                        button.textSize = 24f
                        button.setAllCaps(true)
                        button.letterSpacing = 0.05f
                        button.setShadowLayer(8f, 0f, 4f, Color.parseColor("#000000"))
                        button.setPadding(50, 50, 50, 50)

                        val params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        params.setMargins(0, 0, 0, 32)
                        button.layoutParams = params

                        button.setOnClickListener {
                            val intent = Intent(this@CategoryActivity, QuizActivity::class.java)
                            intent.putExtra("category_id", id)
                            intent.putExtra("username", username)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        }

                        layout.addView(button)
                    }

                    // ukryj spinner, pokaż zawartość z animacją
                    loadingSpinner.visibility = View.GONE
                    contentLayout.visibility = View.VISIBLE
                    contentLayout.alpha = 0f
                    contentLayout.animate().alpha(1f).setDuration(300).start()
                }
            }
        })
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}