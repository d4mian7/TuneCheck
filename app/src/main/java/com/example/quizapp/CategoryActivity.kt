package com.example.quizapp

import android.os.Bundle
import android.view.View
import android.widget.TextView
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
    private val URL = ApiClient.BASE_URL + "get_categories.php"

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

                        val item = layoutInflater.inflate(R.layout.item_category, layout, false)
                        item.findViewById<TextView>(R.id.tvMonogram).text =
                            name.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "?"
                        item.findViewById<TextView>(R.id.tvCategoryName).text = name.uppercase()

                        val params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        params.setMargins(0, 0, 0, (16 * resources.displayMetrics.density).toInt())
                        item.layoutParams = params

                        item.setOnClickListener {
                            val intent = Intent(this@CategoryActivity, QuizActivity::class.java)
                            intent.putExtra("category_id", id)
                            intent.putExtra("username", username)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        }

                        layout.addView(item)
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