package com.example.quizapp

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Testy jednostkowe logiki aplikacji, która nie wymaga telefonu ani serwera.
 * Uruchomienie: ./gradlew test (raport: app/build/reports/tests/testDebugUnitTest/index.html)
 */
class QuizLogicTest {

    // --- tekst w kółku kafelka kategorii (CategoryActivity.monogram) ---

    @Test
    fun monogram_artysta_zwykla_nazwa_daje_pierwsza_litere() {
        assertEquals("D", CategoryActivity.monogram("Drake"))
        assertEquals("N", CategoryActivity.monogram("Notorious B.I.G."))
    }

    @Test
    fun monogram_liczba_na_poczatku_nazwy_daje_te_liczbe() {
        assertEquals("21", CategoryActivity.monogram("21 Savage"))
        assertEquals("50", CategoryActivity.monogram("50 Cent"))
        assertEquals("2", CategoryActivity.monogram("2Pac"))
    }

    @Test
    fun monogram_dekada_w_nazwie_daje_dwie_ostatnie_cyfry() {
        assertEquals("10", CategoryActivity.monogram("Lata 2010."))
        assertEquals("90", CategoryActivity.monogram("Lata 90."))
    }

    @Test
    fun monogram_mala_litera_i_spacje_sa_poprawiane() {
        assertEquals("Y", CategoryActivity.monogram("  yeat "))
    }

    @Test
    fun monogram_pusta_nazwa_daje_znak_zapytania() {
        assertEquals("?", CategoryActivity.monogram("   "))
    }

    // --- podpis pod wynikiem (ResultActivity.scoreCaption) ---

    @Test
    fun podpis_wyniku_ma_poprawna_odmiane() {
        assertEquals("POPRAWNYCH", ResultActivity.scoreCaption(0))
        assertEquals("POPRAWNA", ResultActivity.scoreCaption(1))
        assertEquals("POPRAWNE", ResultActivity.scoreCaption(2))
        assertEquals("POPRAWNE", ResultActivity.scoreCaption(4))
        assertEquals("POPRAWNYCH", ResultActivity.scoreCaption(5))
    }

    // --- bezpieczne odczytywanie odpowiedzi serwera (ApiClient) ---

    @Test
    fun lista_z_serwera_jest_odczytana() {
        val json = """[{"id":"1","name":"Drake","theme_order":null},{"id":"2","name":"Lata 90.","theme_order":"4"}]"""
        val array = ApiClient.jsonArrayOrNull(json)
        assertNotNull(array)
        assertEquals(2, array!!.length())
        assertEquals("Drake", array.getJSONObject(0).getString("name"))
        assertEquals(true, array.getJSONObject(0).isNull("theme_order"))
    }

    @Test
    fun komunikat_bledu_zamiast_listy_daje_null_a_nie_awarie() {
        // tak odpowiada get_categories.php, gdy baza danych jest wyłączona
        assertNull(ApiClient.jsonArrayOrNull("""{"error":"Błąd połączenia z bazą"}"""))
    }

    @Test
    fun pusta_lub_uszkodzona_odpowiedz_daje_null() {
        assertNull(ApiClient.jsonArrayOrNull(null))
        assertNull(ApiClient.jsonArrayOrNull(""))
        assertNull(ApiClient.jsonObjectOrNull("<html>500 Internal Server Error</html>"))
    }

    @Test
    fun obiekt_ze_statusem_jest_odczytany() {
        val obj = ApiClient.jsonObjectOrNull("""{"status":"error","message":"Brak uprawnień"}""")
        assertNotNull(obj)
        assertEquals("error", obj!!.optString("status"))
        assertEquals("Brak uprawnień", obj.optString("message"))
    }
}
