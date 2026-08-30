package com.example.quizapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class AdminQuestionsActivity : AppCompatActivity() {

    private val client = ApiClient.client
    private lateinit var spinnerCategory: Spinner
    private lateinit var questionsList: LinearLayout
    private lateinit var tvQuestionCount: TextView
    private lateinit var btnAddQuestion: Button

    private var categories = mutableListOf<JSONObject>()
    private var selectedCategoryId = -1
    private var selectedCategoryName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_questions)

        spinnerCategory = findViewById(R.id.spinnerCategory)
        questionsList = findViewById(R.id.questionsList)
        tvQuestionCount = findViewById(R.id.tvQuestionCount)
        btnAddQuestion = findViewById(R.id.btnAddQuestion)
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnAddQuestion.setOnClickListener {
            if (selectedCategoryId == -1) {
                AppToast.show(this, "Najpierw wybierz kategorię")
                return@setOnClickListener
            }
            val intent = Intent(this, AddQuestionActivity::class.java)
            intent.putExtra("category_id", selectedCategoryId)
            intent.putExtra("category_name", selectedCategoryName)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btnBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        loadCategories()
    }

    override fun onResume() {
        super.onResume()
        if (selectedCategoryId != -1) {
            loadQuestions(selectedCategoryId)
        }
    }

    private fun loadCategories() {
        val request = Request.Builder()
            .url(ApiClient.BASE_URL + "get_categories.php")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    AppToast.show(this@AdminQuestionsActivity, "Błąd połączenia")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string() ?: return
                val jsonArray = JSONArray(json)

                categories.clear()
                val names = mutableListOf<String>()
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    categories.add(obj)
                    names.add(obj.getString("name"))
                }

                runOnUiThread {
                    val adapter = object : ArrayAdapter<String>(
                        this@AdminQuestionsActivity,
                        android.R.layout.simple_spinner_item,
                        names
                    ) {
                        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                            val view = super.getView(position, convertView, parent) as TextView
                            view.setTextColor(Color.WHITE)
                            view.textSize = 15f
                            return view
                        }

                        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                            val view = super.getDropDownView(position, convertView, parent) as TextView
                            view.setTextColor(Color.WHITE)
                            view.setBackgroundColor(Color.parseColor("#1A1A2B"))
                            view.setPadding(24, 20, 24, 20)
                            view.textSize = 15f
                            return view
                        }
                    }
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerCategory.adapter = adapter

                    spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            val cat = categories[position]
                            selectedCategoryId = cat.getInt("id")
                            selectedCategoryName = cat.getString("name")
                            loadQuestions(selectedCategoryId)
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                }
            }
        })
    }

    private fun loadQuestions(categoryId: Int) {
        val request = Request.Builder()
            .url("${ApiClient.BASE_URL}get_questions_admin.php?category_id=$categoryId")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    AppToast.show(this@AdminQuestionsActivity, "Błąd połączenia")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string() ?: return
                val jsonArray = JSONArray(json)

                runOnUiThread {
                    questionsList.removeAllViews()
                    tvQuestionCount.text = "PYTANIA: ${jsonArray.length()}"

                    if (jsonArray.length() == 0) {
                        val emptyText = TextView(this@AdminQuestionsActivity).apply {
                            text = "Brak pytań w tej kategorii"
                            setTextColor(Color.parseColor("#BBBBBB"))
                            textSize = 15f
                            gravity = Gravity.CENTER
                            setPadding(0, 48, 0, 48)
                        }
                        questionsList.addView(emptyText)
                        return@runOnUiThread
                    }

                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        addQuestionRow(obj)
                    }
                }
            }
        })
    }

    private fun addQuestionRow(obj: JSONObject) {
        val id = obj.getInt("id")
        val questionText = obj.getString("question_text")
        val correct = obj.getString("correct_answer")

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

        val textContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val tvQuestion = TextView(this).apply {
            text = questionText
            setTextColor(Color.WHITE)
            typeface = androidx.core.content.res.ResourcesCompat.getFont(context, R.font.manrope_semibold)
            textSize = 14f
            maxLines = 2
        }

        val tvCorrect = TextView(this).apply {
            text = "POPRAWNA: $correct"
            setTextColor(Color.parseColor("#F5B04C"))
            typeface = androidx.core.content.res.ResourcesCompat.getFont(context, R.font.manrope_extrabold)
            letterSpacing = 0.1f
            textSize = 11f
            setPadding(0, 8, 0, 0)
        }

        textContainer.addView(tvQuestion)
        textContainer.addView(tvCorrect)

        val density = resources.displayMetrics.density
        val btnDelete = android.widget.ImageButton(this).apply {
            setImageResource(R.drawable.ic_trash)
            background = GlowBackgrounds.trash(context)
            scaleType = android.widget.ImageView.ScaleType.CENTER
            stateListAnimator = android.animation.AnimatorInflater.loadStateListAnimator(context, R.animator.press_scale)
            contentDescription = "Usuń pytanie"
            val size = (40 * density).toInt()
            val params = LinearLayout.LayoutParams(size, size)
            params.marginStart = 12
            layoutParams = params
        }
        row.clipChildren = false
        row.clipToPadding = false
        questionsList.clipChildren = false

        btnDelete.setOnClickListener {
            AppDialogs.confirm(this, "Usunąć pytanie?", questionText) { deleteQuestion(id) }
        }

        row.addView(textContainer)
        row.addView(btnDelete)
        questionsList.addView(row)
    }

    private fun deleteQuestion(id: Int) {
        val formBody = FormBody.Builder()
            .add("id", id.toString())
            .build()

        val request = Request.Builder()
            .url(ApiClient.BASE_URL + "delete_question.php")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    AppToast.show(this@AdminQuestionsActivity, "Błąd połączenia")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string() ?: ""

                runOnUiThread {
                    try {
                        val obj = JSONObject(json)
                        if (obj.getString("status") == "ok") {
                            AppToast.show(this@AdminQuestionsActivity, "Pytanie usunięte")
                        } else {
                            AppToast.show(this@AdminQuestionsActivity, obj.getString("message"))
                        }
                    } catch (e: Exception) {
                        AppToast.show(this@AdminQuestionsActivity, "Błąd odpowiedzi serwera")
                    }
                    loadQuestions(selectedCategoryId)
                }
            }
        })
    }
}