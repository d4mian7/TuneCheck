package com.example.quizapp

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class AdminCategoriesActivity : AppCompatActivity() {

    private val client = ApiClient.client
    private lateinit var categoriesList: LinearLayout
    private lateinit var etNewCategory: EditText
    private lateinit var btnAddCategory: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_categories)

        categoriesList = findViewById(R.id.categoriesList)
        etNewCategory = findViewById(R.id.etNewCategory)
        btnAddCategory = findViewById(R.id.btnAddCategory)
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnAddCategory.setOnClickListener { addCategory() }
        btnBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        loadCategories()
    }

    private fun loadCategories() {
        val request = Request.Builder()
            .url(ApiClient.BASE_URL + "get_categories.php")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    AppToast.show(this@AdminCategoriesActivity, "Błąd połączenia")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string() ?: return
                val jsonArray = JSONArray(json)

                runOnUiThread {
                    categoriesList.removeAllViews()

                    if (jsonArray.length() == 0) {
                        val emptyText = TextView(this@AdminCategoriesActivity).apply {
                            text = "Brak kategorii"
                            setTextColor(Color.parseColor("#BBBBBB"))
                            textSize = 15f
                            gravity = Gravity.CENTER
                            setPadding(0, 48, 0, 48)
                        }
                        categoriesList.addView(emptyText)
                        return@runOnUiThread
                    }

                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        addCategoryRow(obj.getInt("id"), obj.getString("name"))
                    }
                }
            }
        })
    }

    private fun addCategoryRow(id: Int, name: String) {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setBackgroundResource(R.drawable.admin_list_item_bg)
            setPadding(24, 20, 16, 20)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.bottomMargin = 12
            layoutParams = params
        }

        val tvName = TextView(this).apply {
            text = name
            setTextColor(Color.WHITE)
            typeface = androidx.core.content.res.ResourcesCompat.getFont(context, R.font.manrope_semibold)
            textSize = 16f
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val density = resources.displayMetrics.density
        val btnDelete = android.widget.ImageButton(this).apply {
            setImageResource(R.drawable.ic_trash)
            background = GlowBackgrounds.trash(context)
            scaleType = android.widget.ImageView.ScaleType.CENTER
            stateListAnimator = android.animation.AnimatorInflater.loadStateListAnimator(context, R.animator.press_scale)
            contentDescription = "Usuń kategorię"
            layoutParams = LinearLayout.LayoutParams((40 * density).toInt(), (40 * density).toInt())
        }
        row.clipChildren = false
        row.clipToPadding = false
        categoriesList.clipChildren = false

        btnDelete.setOnClickListener {
            AppDialogs.confirm(
                this,
                "Usunąć kategorię?",
                "\"$name\" zostanie usunięta wraz ze wszystkimi pytaniami i wynikami."
            ) { deleteCategory(id) }
        }

        row.addView(tvName)
        row.addView(btnDelete)
        categoriesList.addView(row)
    }

    private fun addCategory() {
        val name = etNewCategory.text.toString().trim()
        if (name.isEmpty()) {
            AppToast.show(this, "Podaj nazwę kategorii")
            return
        }

        btnAddCategory.isEnabled = false

        val formBody = FormBody.Builder()
            .add("name", name)
            .build()

        val request = Request.Builder()
            .url(ApiClient.BASE_URL + "add_category.php")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    btnAddCategory.isEnabled = true
                    AppToast.show(this@AdminCategoriesActivity, "Błąd połączenia")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string() ?: return
                val obj = JSONObject(json)

                runOnUiThread {
                    btnAddCategory.isEnabled = true
                    if (obj.getString("status") == "ok") {
                        etNewCategory.text.clear()
                        AppToast.show(this@AdminCategoriesActivity, "Kategoria dodana")
                        loadCategories()
                    } else {
                        AppToast.show(this@AdminCategoriesActivity, obj.getString("message"))
                    }
                }
            }
        })
    }

    private fun deleteCategory(id: Int) {
        val formBody = FormBody.Builder()
            .add("id", id.toString())
            .build()

        val request = Request.Builder()
            .url(ApiClient.BASE_URL + "delete_category.php")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    AppToast.show(this@AdminCategoriesActivity, "Błąd połączenia")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string() ?: ""

                runOnUiThread {
                    try {
                        val obj = JSONObject(json)
                        if (obj.getString("status") == "ok") {
                            AppToast.show(this@AdminCategoriesActivity, "Kategoria usunięta")
                        } else {
                            AppToast.show(this@AdminCategoriesActivity, obj.getString("message"))
                        }
                    } catch (e: Exception) {
                        AppToast.show(this@AdminCategoriesActivity, "Błąd odpowiedzi serwera")
                    }
                    loadCategories()
                }
            }
        })
    }
}