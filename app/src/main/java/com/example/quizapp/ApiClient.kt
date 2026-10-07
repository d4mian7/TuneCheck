package com.example.quizapp

import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object ApiClient {
    // 192.168.1.4 = IP komputera w sieci lokalnej, karta Ethernet (fizyczny telefon);
    // dla emulatora zmień na 10.0.2.2
    const val BASE_URL = "http://192.168.1.4/quiz_api/"

    // token sesji administratora z admin_login.php; wysyłany w nagłówku X-Admin-Token
    // przy każdej operacji panelu admina, null = admin niezalogowany
    var adminToken: String? = null

    val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()

    // odpowiedź serwera jako lista / obiekt JSON; null, gdy serwer zwrócił coś innego
    // (np. komunikat błędu bazy) - dzięki temu aplikacja pokazuje błąd zamiast się zamknąć
    fun jsonArrayOrNull(json: String?): JSONArray? =
        try { JSONArray(json ?: "") } catch (e: JSONException) { null }

    fun jsonObjectOrNull(json: String?): JSONObject? =
        try { JSONObject(json ?: "") } catch (e: JSONException) { null }
}
