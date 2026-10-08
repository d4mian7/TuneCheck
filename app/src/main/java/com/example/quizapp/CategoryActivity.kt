package com.example.quizapp

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import android.util.Log
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException
import android.content.Intent

class CategoryActivity : AppCompatActivity() {

    companion object {
        // tekst w kółku kafelka: liczba z początku nazwy ("21 Savage" -> 21), dekada z nazwy
        // ("Lata 2010." -> 10), w pozostałych przypadkach pierwsza litera
        fun monogram(name: String): String {
            val trimmed = name.trim()
            val number = Regex("\\d+").find(trimmed)?.value
            return when {
                number != null && trimmed.startsWith(number) -> number.take(2)
                number != null -> number.takeLast(2)
                else -> trimmed.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
            }
        }
    }

    private val client = ApiClient.client
    private val URL = ApiClient.BASE_URL + "get_categories.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val username = intent.getStringExtra("username") ?: ""
        val gridThemes = findViewById<GridLayout>(R.id.gridThemes)
        val gridArtists = findViewById<GridLayout>(R.id.gridArtists)
        val contentLayout = findViewById<LinearLayout>(R.id.contentLayout)
        val loadingSpinner = findViewById<ProgressBar>(R.id.loadingSpinner)

        // powrót do ekranu startowego
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        val request = Request.Builder().url(URL).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { showLoadError(contentLayout, loadingSpinner) }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                Log.d("QUIZ", "JSON = $json")

                val jsonArray = ApiClient.jsonArrayOrNull(json)
                if (jsonArray == null) {
                    runOnUiThread { showLoadError(contentLayout, loadingSpinner) }
                    return
                }

                runOnUiThread {
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val id = obj.getInt("id")
                        val name = obj.getString("name")
                        // theme_order = null -> kategoria artysty, w przeciwnym razie tematyczna
                        val grid = if (obj.isNull("theme_order")) gridArtists else gridThemes

                        val item = layoutInflater.inflate(R.layout.item_category, grid, false)
                        item.findViewById<TextView>(R.id.tvMonogram).text = monogram(name)
                        item.findViewById<TextView>(R.id.tvCategoryName).text = name.uppercase()

                        // kafelek zajmuje pół szerokości (waga 1 w kolumnie) i wypełnia wysokość wiersza
                        val params = GridLayout.LayoutParams(
                            GridLayout.spec(GridLayout.UNDEFINED),
                            GridLayout.spec(GridLayout.UNDEFINED, 1f)
                        )
                        params.width = 0
                        params.setGravity(Gravity.FILL)
                        val margin = (5 * resources.displayMetrics.density).toInt()
                        params.setMargins(margin, margin, margin, margin)
                        item.layoutParams = params

                        item.setOnClickListener {
                            val intent = Intent(this@CategoryActivity, QuizActivity::class.java)
                            intent.putExtra("category_id", id)
                            intent.putExtra("category_name", name)
                            intent.putExtra("username", username)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        }

                        grid.addView(item)
                    }

                    findViewById<TextView>(R.id.tvThemesCount).text = gridThemes.childCount.toString()
                    findViewById<TextView>(R.id.tvArtistsCount).text = gridArtists.childCount.toString()

                    // ukryj spinner, pokaż zawartość z animacją
                    loadingSpinner.visibility = View.GONE
                    contentLayout.visibility = View.VISIBLE
                    contentLayout.alpha = 0f
                    contentLayout.animate().alpha(1f).setDuration(300).start()
                }
            }
        })
    }

    // brak połączenia lub błąd serwera: zamiast kręcącego się spinnera pokaż nagłówek
    // (z przyciskiem powrotu) i komunikat
    private fun showLoadError(contentLayout: View, loadingSpinner: View) {
        loadingSpinner.visibility = View.GONE
        contentLayout.visibility = View.VISIBLE
        AppToast.show(this, "Nie udało się pobrać kategorii")
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}