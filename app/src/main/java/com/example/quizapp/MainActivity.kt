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
import android.widget.Toast
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val client = ApiClient.client

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStart = findViewById<Button>(R.id.btnStart)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val btnAdmin = findViewById<ImageView>(R.id.btnAdmin)

        btnStart.setOnClickListener {
            val username = etUsername.text.toString().trim()

            if (username.isEmpty()) {
                Toast.makeText(this, "Podaj nazwę użytkownika", Toast.LENGTH_SHORT).show()
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
        // Preload - rozgrzewamy połączenie z serwerem
        Thread {
            try {
                val request = Request.Builder()
                    .url("http://10.0.2.2/quiz_api/get_categories.php")
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
        dialog.setCancelable(true)

        val etLogin = dialog.findViewById<EditText>(R.id.etAdminLogin)
        val etPassword = dialog.findViewById<EditText>(R.id.etAdminPassword)
        val btnLogin = dialog.findViewById<Button>(R.id.btnAdminLogin)

        btnLogin.setOnClickListener {
            val login = etLogin.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Podaj login i hasło", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnLogin.isEnabled = false
            btnLogin.text = "LOGOWANIE..."

            val formBody = FormBody.Builder()
                .add("username", login)
                .add("password", password)
                .build()

            val request = Request.Builder()
                .url("http://10.0.2.2/quiz_api/admin_login.php")
                .post(formBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        btnLogin.isEnabled = true
                        btnLogin.text = "ZALOGUJ"
                        Toast.makeText(this@MainActivity, "Błąd połączenia z serwerem", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val json = response.body?.string() ?: return
                    val obj = JSONObject(json)

                    runOnUiThread {
                        if (obj.getString("status") == "ok") {
                            dialog.dismiss()
                            Toast.makeText(this@MainActivity, "Zalogowano jako admin", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@MainActivity, AdminPanelActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        } else {
                            btnLogin.isEnabled = true
                            btnLogin.text = "ZALOGUJ"
                            Toast.makeText(this@MainActivity, obj.getString("message"), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }

        dialog.show()
    }
}