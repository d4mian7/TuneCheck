package com.example.quizapp

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class AddQuestionActivity : AppCompatActivity() {

    private val client = ApiClient.client
    private var categoryId = -1

    private lateinit var etQuestionText: EditText
    private lateinit var etAnswerA: EditText
    private lateinit var etAnswerB: EditText
    private lateinit var etAnswerC: EditText
    private lateinit var etAnswerD: EditText
    private lateinit var spinnerCorrectAnswer: Spinner
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_question)

        categoryId = intent.getIntExtra("category_id", -1)
        val categoryName = intent.getStringExtra("category_name") ?: ""

        val tvCategoryName = findViewById<TextView>(R.id.tvCategoryName)
        tvCategoryName.text = "Kategoria: $categoryName"

        etQuestionText = findViewById(R.id.etQuestionText)
        etAnswerA = findViewById(R.id.etAnswerA)
        etAnswerB = findViewById(R.id.etAnswerB)
        etAnswerC = findViewById(R.id.etAnswerC)
        etAnswerD = findViewById(R.id.etAnswerD)
        spinnerCorrectAnswer = findViewById(R.id.spinnerCorrectAnswer)
        btnSave = findViewById(R.id.btnSaveQuestion)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        // Spinner z opcjami A, B, C, D
        val answers = listOf("A", "B", "C", "D")
        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            answers
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
        spinnerCorrectAnswer.adapter = adapter

        btnSave.setOnClickListener { saveQuestion() }
        btnCancel.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    private fun saveQuestion() {
        val questionText = etQuestionText.text.toString().trim()
        val answerA = etAnswerA.text.toString().trim()
        val answerB = etAnswerB.text.toString().trim()
        val answerC = etAnswerC.text.toString().trim()
        val answerD = etAnswerD.text.toString().trim()
        val correctAnswer = spinnerCorrectAnswer.selectedItem.toString()

        if (questionText.isEmpty() || answerA.isEmpty() || answerB.isEmpty()
            || answerC.isEmpty() || answerD.isEmpty()) {
            Toast.makeText(this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show()
            return
        }

        btnSave.isEnabled = false
        btnSave.text = "ZAPISYWANIE..."

        val formBody = FormBody.Builder()
            .add("category_id", categoryId.toString())
            .add("question_text", questionText)
            .add("answerA", answerA)
            .add("answerB", answerB)
            .add("answerC", answerC)
            .add("answerD", answerD)
            .add("correct_answer", correctAnswer)
            .build()

        val request = Request.Builder()
            .url("http://10.0.2.2/quiz_api/add_question.php")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    btnSave.isEnabled = true
                    btnSave.text = "ZAPISZ PYTANIE"
                    Toast.makeText(this@AddQuestionActivity, "Błąd połączenia", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string() ?: return
                val obj = JSONObject(json)

                runOnUiThread {
                    if (obj.getString("status") == "ok") {
                        Toast.makeText(this@AddQuestionActivity, "Pytanie dodane!", Toast.LENGTH_SHORT).show()
                        finish()
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    } else {
                        btnSave.isEnabled = true
                        btnSave.text = "ZAPISZ PYTANIE"
                        Toast.makeText(this@AddQuestionActivity, obj.getString("message"), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}