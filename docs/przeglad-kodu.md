# Przegląd kodu TuneCheck (stan na 07.10.2026, commit e13e2d0)

Materiał roboczy do spisu treści, nagrania na seminarium i rozdziałów o implementacji i ograniczeniach.

## 1. Rozmiar i struktura

| Warstwa | Pliki | Linie kodu |
|---|---|---|
| Aplikacja Android (Kotlin) | 12 plików w `app/src/main/java/com/example/quizapp/` | ok. 1640 |
| Backend (PHP) | 10 endpointów + `auth.php` w `quiz_api/` | ok. 365 |
| Baza danych (MariaDB/MySQL) | 4 tabele w bazie `quizdb` | — |
| Interfejs | 14 layoutów XML, drawable, animacje, czcionki | — |

Architektura: **klient–serwer**. Aplikacja wysyła żądania HTTP (OkHttp), serwer PHP odpowiada w JSON, dane są w MySQL.

```
[Android / Kotlin] --HTTP (GET/POST), JSON--> [PHP: quiz_api/*.php] --SQL--> [MySQL: quizdb]
```

## 2. Moduły aplikacji

| Plik | Rola |
|---|---|
| `ApiClient.kt` | Wspólny klient HTTP (OkHttp, limity czasu 5 s) i adres serwera `BASE_URL` |
| `MainActivity.kt` | Ekran startowy: nick gracza, przycisk ZAGRAJ, okno logowania administratora |
| `CategoryActivity.kt` | Pobranie kategorii, siatka „Tematy” i „Artyści”, przycisk powrotu |
| `QuizActivity.kt` | Rdzeń gry: odliczanie 3-2-1, 5 losowych pytań, licznik 10 s, ocena odpowiedzi, punktacja |
| `ResultActivity.kt` | Wynik (pierścień postępu), zapis wyniku, zagraj ponownie, powrót do kategorii |
| `AdminPanelActivity.kt` | Menu administratora |
| `AdminCategoriesActivity.kt` | Lista, dodawanie i usuwanie kategorii |
| `AdminQuestionsActivity.kt` | Lista pytań w kategorii, usuwanie pytań |
| `AddQuestionActivity.kt` | Formularz nowego pytania (4 odpowiedzi + poprawna A–D) |
| `AppDialogs.kt`, `AppToast.kt` | Własne okno potwierdzenia i komunikaty w stylu aplikacji |
| `GlowBackgrounds.kt` | Rysowanie tła przycisków odpowiedzi i kosza z poświatą |

## 3. Endpointy API

| Endpoint | Metoda | Wejście | Wynik | Użycie |
|---|---|---|---|---|
| `get_categories.php` | GET | — | lista `{id, name, theme_order}` | gracz, admin |
| `get_questions.php` | GET | `category_id`, `limit` | losowe pytania (`ORDER BY RAND()`) | gracz |
| `save_score.php` | POST | `username`, `category_id`, `score`, `total` | `{status}` | gracz |
| `admin_login.php` | POST | `username`, `password` | `{status, admin_id, token}` (bcrypt `password_verify`) | admin |
| `admin_logout.php` | POST | nagłówek `X-Admin-Token` | kasuje token | admin |
| `get_questions_admin.php` | GET | `category_id` + token | wszystkie pytania kategorii | admin |
| `add_category.php` | POST | `name` + token | `{status, id}` | admin |
| `delete_category.php` | POST | `id` + token | usuwa kategorię z pytaniami i wynikami | admin |
| `add_question.php` | POST | token, treść, 4 odpowiedzi, `correct_answer` ∈ {A,B,C,D} | `{status, id}` | admin |
| `delete_question.php` | POST | `id` + token | `{status}` | admin |

## 4. Baza danych

| Tabela | Kolumny | Uwagi |
|---|---|---|
| `categories` | id, name (unikalna), theme_order | `theme_order` NULL = artysta, liczba = kategoria tematyczna i jej kolejność |
| `questions` | id, category_id, question_text, answerA–D, correct_answer, image_url | klucz obcy do `categories`; `image_url` nieużywana |
| `scores` | id, username, category_id, score, total, created_at | **brak klucza obcego** do `categories` |
| `admins` | id, username (unikalny), password (hash bcrypt), token (sesja, NULL = wylogowany), created_at | 1 konto |

Dane: 44 kategorie (8 tematycznych + 36 artystów), 440 pytań (po 10 na kategorię, gra losuje 5).

## 5. Bezpieczeństwo

**Dobrze:**
- Hasło administratora przechowywane jako hash **bcrypt**, sprawdzane po stronie serwera (`password_verify`).
- Ochrona przed SQL injection: zapytania przygotowane (`prepare` + `bind_param`) albo rzutowanie na liczbę (`intval`).
- Walidacja danych po obu stronach (np. puste pola w aplikacji, `correct_answer` tylko A–D na serwerze).
- `error_reporting(0)`: serwer nie pokazuje szczegółów błędów PHP.

**Ograniczenia (sprawdzone 07.10.2026):**
1. ~~Endpointy administratora nie wymagają zalogowania.~~ **Naprawione 07.10.2026: token sesji.**
   `admin_login.php` po poprawnym haśle generuje losowy token (`random_bytes(32)`, 64 znaki hex), zapisuje go
   w `admins.token` i zwraca aplikacji. Aplikacja trzyma go w `ApiClient.adminToken` i wysyła w nagłówku
   `X-Admin-Token` przy operacjach panelu. 5 endpointów admina dołącza `auth.php`, który bez poprawnego tokenu
   odpowiada „Brak uprawnień”. `admin_logout.php` kasuje token. Sprawdzone 8 żądaniami `curl`: brak lub zły
   token → odmowa, dobry token → działa, po wylogowaniu stary token → odmowa, gra bez tokenu działa normalnie.
   Pozostałe ograniczenie: token idzie przez HTTP, więc w tej samej sieci da się go podsłuchać (patrz pkt 2).
2. Połączenie przez **HTTP, nie HTTPS** (`usesCleartextTraffic`). Akceptowalne w sieci lokalnej, nie do publicznego wdrożenia.
3. Baza: użytkownik `root` bez hasła, dane połączenia powtórzone w każdym pliku PHP.

## 6. Znalezione błędy

**Wszystkie naprawione 07.10.2026** (nowe funkcje `ApiClient.jsonArrayOrNull` / `jsonObjectOrNull`,
`QuizActivity.showLoadError`, `CategoryActivity.showLoadError`, poprawka w `save_score.php`).
Błąd 1 potwierdzony po poprawce żądaniem `curl` (score=0 → `{"status":"ok"}`); pozostałe do sprawdzenia na telefonie.

| # | Błąd | Jak sprawdzone | Skutek |
|---|---|---|---|
| 1 | Wynik **0/5 nie zapisuje się**: `save_score.php` traktuje `0` jak brak danych (`!$score`) | `curl`: score=0 → „Brak danych”, score=3 → ok | wynik 0 przepada |
| 2 | Aplikacja **zawsze** pokazuje „Wynik zapisany”, nie sprawdza `status` z serwera (`ResultActivity`) | analiza kodu | przy błędzie 1 gracz dostaje fałszywe potwierdzenie |
| 3 | Przycisk zapisu można kliknąć wiele razy | analiza kodu | duplikaty w `scores` |
| 4 | Brak połączenia z serwerem: ekran kategorii i quizu kręci się bez końca, bez komunikatu (`onFailure` tylko loguje) | analiza kodu | gracz nie wie, co się stało |
| 5 | Kategoria bez pytań (np. świeżo dodana przez admina): po odliczaniu spinner bez końca | analiza kodu | gra się zawiesza |
| 6 | Odpowiedź serwera z błędem (np. baza wyłączona) parsowana jako lista (`JSONArray`) bez `try/catch` | analiza kodu, **do potwierdzenia testem** | prawdopodobny crash aplikacji |

## 7. Testy

**Dodane 08.10.2026:**
- **Testy jednostkowe** `app/src/test/java/com/example/quizapp/QuizLogicTest.kt` (JUnit, 10 testów, `./gradlew test`):
  litery w kółkach kategorii (`CategoryActivity.monogram`), odmiana podpisu wyniku (`ResultActivity.scoreCaption`),
  bezpieczne odczytywanie odpowiedzi serwera (`ApiClient.jsonArrayOrNull` / `jsonObjectOrNull`). Wynik: 10/10.
- **Testy integracyjne** `tests/api_test.php` (PHP, 31 testów, `C:\xampp\php\php.exe tests\api_test.php`):
  prawdziwe żądania HTTP do API + sprawdzenie w bazie zapisu, odczytu i usuwania danych; bezpieczeństwo
  (złe hasło, SQL injection, brak/zły/stary token). Wynik: 31/31; po teście baza wraca do stanu sprzed testu.
- Kontrola, że testy wykrywają błędy: po celowym przywróceniu błędu „wynik 0/5” 2 testy zgłosiły BŁĄD (29/31).
- Testy akceptacyjne: ręczna lista 19 scenariuszy na telefonie (07.10.2026), wszystkie zaliczone po poprawkach.

## 8. Niespójności w dokumentacji

- ~~`README.md` wspomina „ranking”~~ — usunięte 07.10.2026; ranking jako kierunek rozwoju (wyniki są w bazie, brak ekranu).
- `CLAUDE.md` podaje adres emulatora `10.0.2.2` (obecnie `192.168.1.4`) i nieistniejący już test `ExampleUnitTest`.

## 9. Mocne strony do pokazania

- Prosta, czytelna architektura: każdy ekran to osobna aktywność, każdy endpoint to osobny plik.
- Ten sam wzorzec sieciowy wszędzie: `enqueue` → parsowanie JSON → `runOnUiThread`.
- Pytania pobierają się w tle podczas odliczania 3-2-1, więc gracz nie czeka.
- Panel administratora w aplikacji: zarządzanie treścią bez dostępu do bazy.
- Baza pytań zweryfikowana w źródłach, bez podpowiedzi między pytaniami (skrypt sprawdzający).
