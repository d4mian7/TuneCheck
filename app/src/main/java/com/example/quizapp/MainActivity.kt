package com.example.quizapp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    companion object {
        /** Ustawiane przez AdminPanelActivity przy wylogowaniu. */
        var showLogoutMessage = false
    }

    private val client = ApiClient.client

    override fun onResume() {
        super.onResume()
        if (showLogoutMessage) {
            showLogoutMessage = false
            // schowaj klawiaturę i zdejmij fokus z pola, żeby komunikat był widoczny
            findViewById<EditText>(R.id.etUsername)?.clearFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
            imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
            window.decorView.postDelayed({ AppToast.show(this, "Wylogowano z panelu admina") }, 500)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStart = findViewById<Button>(R.id.btnStart)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val btnAdmin = findViewById<android.view.View>(R.id.btnAdmin)

        btnStart.setOnClickListener {
            val username = etUsername.text.toString().trim()

            if (username.isEmpty()) {
                AppToast.show(this, "Podaj nazwę użytkownika")
                return@setOnClickListener
            }

            val intent = Intent(this, CategoryActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btnAdmin.setOnClickListener {
            showAdminLoginDialog()
        }

        // gdy klawiatura jest widoczna, unieś całą scenę (tło + panel), by przycisk był tuż nad nią
        val contentRoot = findViewById<android.view.View>(R.id.contentRoot)
        val density = resources.displayMetrics.density
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(android.R.id.content)
        ) { _, insets ->
            val ime = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.ime()).bottom
            val lift = ime - 106 * density
            contentRoot.translationY = if (lift > 0) -lift else 0f
            insets
        }
        // Preload - rozgrzewamy połączenie z serwerem
        Thread {
            try {
                val request = Request.Builder()
                    .url(ApiClient.BASE_URL + "get_categories.php")
                    .build()
                ApiClient.client.newCall(request).execute().close()
            } catch (_: Exception) {}
        }.start()
    }

    private fun showAdminLoginDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_admin_login)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.88).toInt(),
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(true)

        val etLogin = dialog.findViewById<EditText>(R.id.etAdminLogin)
        val etPassword = dialog.findViewById<EditText>(R.id.etAdminPassword)
        val btnLogin = dialog.findViewById<Button>(R.id.btnAdminLogin)
        val tvError = dialog.findViewById<android.widget.TextView>(R.id.tvLoginError)

        fun showError(message: String) {
            tvError.text = message
            tvError.visibility = android.view.View.VISIBLE
        }

        btnLogin.setOnClickListener {
            val login = etLogin.text.toString().trim()
            val password = etPassword.text.toString().trim()
            tvError.visibility = android.view.View.GONE

            if (login.isEmpty() || password.isEmpty()) {
                showError("Podaj login i hasło")
                return@setOnClickListener
            }

            btnLogin.isEnabled = false
            btnLogin.text = "LOGOWANIE..."

            val formBody = FormBody.Builder()
                .add("username", login)
                .add("password", password)
                .build()

            val request = Request.Builder()
                .url(ApiClient.BASE_URL + "admin_login.php")
                .post(formBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        btnLogin.isEnabled = true
                        btnLogin.text = "ZALOGUJ"
                        showError("Błąd połączenia z serwerem")
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val json = response.body?.string() ?: return
                    val obj = JSONObject(json)

                    runOnUiThread {
                        if (obj.getString("status") == "ok") {
                            dialog.dismiss()
                            val intent = Intent(this@MainActivity, AdminPanelActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        } else {
                            btnLogin.isEnabled = true
                            btnLogin.text = "ZALOGUJ"
                            showError(obj.getString("message"))
                        }
                    }
                }
            })
        }

        dialog.show()
    }
}