package com.example.quizapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import kotlin.math.ceil

class QuizActivity : AppCompatActivity() {

    private val MAX_QUESTIONS = 5
    private val QUESTION_TIME_MS = 10_000L
    private val REVEAL_DELAY_CLICK = 1500L
    private val REVEAL_DELAY_TIMEOUT = 2500L

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
    private lateinit var dotsContainer: LinearLayout
    private lateinit var timerFill: View
    private lateinit var timerChip: LinearLayout
    private lateinit var tvTimer: TextView
    private lateinit var ivTimerIcon: ImageView

    private lateinit var answers: List<LinearLayout>
    private lateinit var badges: List<TextView>
    private lateinit var labels: List<TextView>
    private lateinit var checks: List<ImageView>

    private var timer: CountDownTimer? = null
    private val handler = Handler(Looper.getMainLooper())

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
        dotsContainer = findViewById(R.id.dotsContainer)
        timerFill = findViewById(R.id.timerFill)
        timerChip = findViewById(R.id.timerChip)
        tvTimer = findViewById(R.id.tvTimer)
        ivTimerIcon = findViewById(R.id.ivTimerIcon)

        answers = listOf(
            findViewById(R.id.answerA), findViewById(R.id.answerB),
            findViewById(R.id.answerC), findViewById(R.id.answerD)
        )
        badges = listOf(
            findViewById(R.id.tvBadgeA), findViewById(R.id.tvBadgeB),
            findViewById(R.id.tvBadgeC), findViewById(R.id.tvBadgeD)
        )
        labels = listOf(
            findViewById(R.id.tvLabelA), findViewById(R.id.tvLabelB),
            findViewById(R.id.tvLabelC), findViewById(R.id.tvLabelD)
        )
        checks = listOf(
            findViewById(R.id.ivCheckA), findViewById(R.id.ivCheckB),
            findViewById(R.id.ivCheckC), findViewById(R.id.ivCheckD)
        )

        val letters = listOf("A", "B", "C", "D")
        answers.forEachIndexed { i, view ->
            view.background = GlowBackgrounds.quizAnswer(this)
            view.setOnClickListener { checkAnswer(letters[i]) }
        }

        loadQuestions()
    }

    private fun loadQuestions() {
        val url =
            "${ApiClient.BASE_URL}get_questions.php?category_id=$categoryId&limit=$MAX_QUESTIONS"

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

    // --- Timer ---

    private fun startTimer() {
        timer?.cancel()

        timerFill.pivotX = 0f
        timerFill.scaleX = 1f
        timerFill.setBackgroundColor(getColor(R.color.gold))
        timerChip.setBackgroundResource(R.drawable.chip_timer)
        tvTimer.setTextColor(getColor(R.color.gold))
        tvTimer.text = "10 s"
        ivTimerIcon.clearColorFilter()

        timer = object : CountDownTimer(QUESTION_TIME_MS, 50) {
            override fun onTick(millisUntilFinished: Long) {
                timerFill.pivotX = 0f
                timerFill.scaleX = millisUntilFinished / QUESTION_TIME_MS.toFloat()

                val color = when {
                    millisUntilFinished > 4000 -> getColor(R.color.gold)
                    millisUntilFinished > 1500 -> getColor(R.color.timer_orange)
                    else -> getColor(R.color.timer_red)
                }
                timerFill.setBackgroundColor(color)

                val seconds = ceil(millisUntilFinished / 1000.0).toInt()
                tvTimer.text = "$seconds s"
            }

            override fun onFinish() {
                timerFill.scaleX = 0f
                onTimeUp()
            }
        }.start()
    }

    private fun onTimeUp() {
        disableButtons()

        // chip w trybie "czas minął"
        timerChip.setBackgroundResource(R.drawable.chip_timer_danger)
        tvTimer.setTextColor(Color.parseColor("#FFD3D2"))
        tvTimer.text = "CZAS MINĄŁ"
        ivTimerIcon.setColorFilter(Color.parseColor("#FFD3D2"))

        // pokaż poprawną odpowiedź, resztę wygaś
        val correctIdx = letterIndex(correctAnswer)
        styleCorrect(correctIdx)
        answers.indices.filter { it != correctIdx }.forEach { answers[it].alpha = 0.4f }

        currentIndex++
        scheduleNext(REVEAL_DELAY_TIMEOUT)
    }

    // --- Odpowiedzi ---

    private fun checkAnswer(answer: String) {
        timer?.cancel()

        val clickedIdx = letterIndex(answer)
        val correctIdx = letterIndex(correctAnswer)

        if (clickedIdx == correctIdx) {
            styleCorrect(clickedIdx)
            score++
        } else {
            styleWrong(clickedIdx)
            styleCorrect(correctIdx)
        }

        // wygaś pozostałe odpowiedzi
        answers.indices
            .filter { it != clickedIdx && it != correctIdx }
            .forEach { answers[it].alpha = 0.4f }

        disableButtons()
        currentIndex++
        scheduleNext(REVEAL_DELAY_CLICK)
    }

    private fun scheduleNext(revealDelay: Long) {
        if (currentIndex >= questions.size) {
            handler.postDelayed({
                if (isFinishing || isDestroyed) return@postDelayed
                val slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_left)
                slideOut.fillAfter = true
                questionPanel.startAnimation(slideOut)
                handler.postDelayed({
                    if (isFinishing || isDestroyed) return@postDelayed
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
            }, revealDelay)
        } else {
            handler.postDelayed({
                if (isFinishing || isDestroyed) return@postDelayed
                val slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_left)
                questionPanel.startAnimation(slideOut)
                handler.postDelayed({
                    if (isFinishing || isDestroyed) return@postDelayed
                    showQuestion()
                    val slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
                    questionPanel.startAnimation(slideIn)
                }, 300)
            }, revealDelay)
        }
    }

    // --- Stylowanie odpowiedzi ---

    private fun styleCorrect(i: Int) {
        answers[i].alpha = 1f
        answers[i].setBackgroundResource(R.drawable.btn_answer_correct)
        badges[i].setBackgroundResource(R.drawable.answer_badge_white)
        badges[i].setTextColor(Color.WHITE)
        labels[i].typeface =
            androidx.core.content.res.ResourcesCompat.getFont(this, R.font.manrope_extrabold)
        checks[i].visibility = View.VISIBLE
    }

    private fun styleWrong(i: Int) {
        answers[i].alpha = 1f
        answers[i].setBackgroundResource(R.drawable.btn_answer_wrong)
        badges[i].setBackgroundResource(R.drawable.answer_badge_white)
        badges[i].setTextColor(Color.WHITE)
        checks[i].visibility = View.GONE
    }

    private fun resetAnswerStyles() {
        answers.indices.forEach { i ->
            answers[i].alpha = 1f
            answers[i].isEnabled = true
            answers[i].background = GlowBackgrounds.quizAnswer(this)
            badges[i].setBackgroundResource(R.drawable.answer_badge)
            badges[i].setTextColor(getColor(R.color.gold))
            labels[i].typeface =
                androidx.core.content.res.ResourcesCompat.getFont(this, R.font.manrope_semibold)
            checks[i].visibility = View.GONE
        }
    }

    private fun disableButtons() {
        answers.forEach { it.isEnabled = false }
    }

    private fun letterIndex(letter: String): Int = when (letter) {
        "A" -> 0; "B" -> 1; "C" -> 2; else -> 3
    }

    // --- Postęp ---

    private fun updateProgress() {
        val questionNumber = currentIndex + 1
        tvQuestionCounter.text = "PYTANIE $questionNumber / ${questions.size}"

        dotsContainer.removeAllViews()
        val density = resources.displayMetrics.density
        val size = (8 * density).toInt()
        val gap = (6 * density).toInt()
        for (i in questions.indices) {
            val dot = View(this)
            dot.setBackgroundResource(
                if (i < questionNumber) R.drawable.dot_active else R.drawable.dot_inactive
            )
            val params = LinearLayout.LayoutParams(size, size)
            if (i > 0) params.marginStart = gap
            dot.layoutParams = params
            dotsContainer.addView(dot)
        }
    }

    private fun showQuestion() {
        resetAnswerStyles()
        updateProgress()
        val obj = questions[currentIndex]
        tvQuestion.text = obj.getString("question_text")
        labels[0].text = obj.getString("answerA")
        labels[1].text = obj.getString("answerB")
        labels[2].text = obj.getString("answerC")
        labels[3].text = obj.getString("answerD")
        correctAnswer = obj.getString("correct_answer")
        startTimer()
    }

    override fun onDestroy() {
        timer?.cancel()
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
