package com.example.quizapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import android.content.Intent

class QuizActivity : AppCompatActivity() {

    private val MAX_QUESTIONS = 5
    private var categoryId = -1
    private var currentIndex = 0
    private var score = 0
    private var correctAnswer = ""
    private var username = ""

    private lateinit var questions: List<JSONObject>

    private lateinit var questionPanel: LinearLayout
    private lateinit var loadingSpinner: ProgressBar
    private lateinit var tvQuestion: TextView
    private lateinit var tvQuestionCounter: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnA: Button
    private lateinit var btnB: Button
    private lateinit var btnC: Button
    private lateinit var btnD: Button

    private val client = ApiClient.client

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        categoryId = intent.getIntExtra("category_id", -1)
        username = intent.getStringExtra("username") ?: ""

        questionPanel = findViewById(R.id.questionPanel)
        loadingSpinner = findViewById(R.id.loadingSpinner)
        tvQuestion = findViewById(R.id.tvQuestion)
        tvQuestionCounter = findViewById(R.id.tvQuestionCounter)
        progressBar = findViewById(R.id.progressBar)
        btnA = findViewById(R.id.btnA)
        btnB = findViewById(R.id.btnB)
        btnC = findViewById(R.id.btnC)
        btnD = findViewById(R.id.btnD)

        btnA.setOnClickListener { checkAnswer("A") }
        btnB.setOnClickListener { checkAnswer("B") }
        btnC.setOnClickListener { checkAnswer("C") }
        btnD.setOnClickListener { checkAnswer("D") }

        loadQuestions()
    }

    private fun loadQuestions() {
        val url =
            "http://10.0.2.2/quiz_api/get_questions.php?category_id=$categoryId&limit=$MAX_QUESTIONS"

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { tvQuestion.text = "Błąd pobierania pytań" }
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string() ?: return
                val jsonArray = JSONArray(json)

                val list = mutableListOf<JSONObject>()
                for (i in 0 until jsonArray.length()) {
                    list.add(jsonArray.getJSONObject(i))
                }

                questions = list
                runOnUiThread {
                    loadingSpinner.visibility = View.GONE
                    questionPanel.visibility = View.VISIBLE
                    showQuestion()
                    val slideIn = AnimationUtils.loadAnimation(this@QuizActivity, R.anim.slide_in_right)
                    questionPanel.startAnimation(slideIn)
                }
            }
        })
    }

    private fun setButtonBackground(btn: Button, drawableRes: Int) {
        btn.backgroundTintList = null
        btn.minHeight = 0
        btn.setBackgroundResource(drawableRes)
    }

    private fun updateProgress() {
        val questionNumber = currentIndex + 1
        tvQuestionCounter.text = "Pytanie $questionNumber / ${questions.size}"
        progressBar.max = questions.size
        progressBar.progress = questionNumber
    }

    private fun checkAnswer(answer: String) {
        val clickedBtn = when (answer) {
            "A" -> btnA; "B" -> btnB; "C" -> btnC; else -> btnD
        }

        if (answer == correctAnswer) {
            setButtonBackground(clickedBtn, R.drawable.btn_answer_correct)
            score++
        } else {
            setButtonBackground(clickedBtn, R.drawable.btn_answer_wrong)
            val correctBtn = when (correctAnswer) {
                "A" -> btnA; "B" -> btnB; "C" -> btnC; else -> btnD
            }
            setButtonBackground(correctBtn, R.drawable.btn_answer_correct)
        }

        disableButtons()
        currentIndex++

        if (currentIndex >= questions.size) {
            Handler(Looper.getMainLooper()).postDelayed({
                val slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_left)
                slideOut.fillAfter = true
                questionPanel.startAnimation(slideOut)
                Handler(Looper.getMainLooper()).postDelayed({
                    questionPanel.visibility = View.GONE
                    val intent = Intent(this, ResultActivity::class.java)
                    intent.putExtra("score", score)
                    intent.putExtra("total", questions.size)
                    intent.putExtra("category_id", categoryId)
                    intent.putExtra("username", username)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                }, 300)
            }, 1000)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                val slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_left)
                questionPanel.startAnimation(slideOut)
                Handler(Looper.getMainLooper()).postDelayed({
                    showQuestion()
                    val slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
                    questionPanel.startAnimation(slideIn)
                }, 300)
            }, 1000)
        }
    }

    private fun disableButtons() {
        listOf(btnA, btnB, btnC, btnD).forEach { it.isEnabled = false }
    }

    private fun enableButtons() {
        listOf(btnA, btnB, btnC, btnD).forEach {
            it.isEnabled = true
            it.minHeight = 0
            setButtonBackground(it, R.drawable.quiz_answer_bg)
        }
    }

    private fun showQuestion() {
        enableButtons()
        updateProgress()
        val obj = questions[currentIndex]
        tvQuestion.text = obj.getString("question_text")
        btnA.text = obj.getString("answerA")
        btnB.text = obj.getString("answerB")
        btnC.text = obj.getString("answerC")
        btnD.text = obj.getString("answerD")
        correctAnswer = obj.getString("correct_answer")
    }
}