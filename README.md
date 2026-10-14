# TuneCheck 🎵

Mobilna aplikacja quizowa o tematyce muzycznej — **projekt inżynierski** (Informatyka, Uniwersytet Kazimierza Wielkiego w Bydgoszczy).

Architektura **klient–serwer**: natywna aplikacja Android (Kotlin) + backend PHP + relacyjna baza MySQL.

## Funkcje

**Tryb gracza**
- wybór kategorii pobieranych dynamicznie z API,
- rozgrywka: 5 losowych pytań na kategorię, 4 odpowiedzi, natychmiastowy feedback (zielony/czerwony), pasek postępu,
- ekran wyniku z zapisem rezultatu do bazy.

**Tryb administratora** (wbudowany w aplikację — zarządzanie treścią bez dotykania bazy)
- logowanie administratora (weryfikacja hasła **bcrypt** po stronie backendu), operacje panelu chronione **tokenem sesji**,
- CRUD kategorii i pytań z poziomu telefonu.

## Architektura

```
[Android / Kotlin]  --HTTP/JSON-->  [PHP: quiz_api/]  --SQL-->  [MySQL: quizdb]
```

- **Klient:** architektura oparta na Activity; sieć przez OkHttp (singleton `ApiClient`, wzorzec enqueue + `runOnUiThread`), animacje przejść między ekranami, Material 3 (tryb ciemny).
- **Backend:** `quiz_api/` — endpoint-per-file (PHP + mysqli, prepared statements), auth admina przez `password_verify` (bcrypt) i token sesji w nagłówku `X-Admin-Token` (`auth.php`).
- **Baza:** MySQL (`quizdb`) — kategorie, pytania, wyniki, administratorzy.

Kluczowe pliki klienta: `MainActivity` (start + logowanie admina) · `CategoryActivity` · `QuizActivity` (logika rozgrywki) · `ResultActivity` · `AdminPanelActivity` + ekrany CRUD.

## Uruchomienie

1. Backend: XAMPP (Apache + MySQL) → katalog `quiz_api/` do `htdocs/`, baza `quizdb` (MySQL, lokalnie `root` bez hasła — konfiguracja deweloperska).
   Bazę (struktura + 44 kategorie i 440 pytań) tworzy import pliku [`database/quizdb.sql`](database/quizdb.sql):
   `mysql -u root < database/quizdb.sql` albo phpMyAdmin → Import. Konto demo administratora: login `admin`, hasło `tunecheck`.
2. Aplikacja: Android Studio → w `ApiClient.kt` ustaw `BASE_URL` na adres komputera z XAMPP-em:
   telefon w tej samej sieci — adres IP komputera (np. `http://192.168.1.4/quiz_api/`), emulator — `http://10.0.2.2/quiz_api/`.

Konfiguracja: dane połączenia z bazą są w plikach `quiz_api/*.php` (`127.0.0.1`, użytkownik `root`, bez hasła — domyślne ustawienia XAMPP).

```bash
./gradlew assembleDebug   # build
./gradlew test            # testy jednostkowe (app/src/test, 10 testów)
C:\xampp\php\php.exe tests\api_test.php   # testy integracyjne API + bazy (31 testów, wymaga XAMPP)
```

Min SDK 26 · Target SDK 36 · Java 11.

## Wersja

**1.0** (tag `v1.0`, 14.10.2026) — wersja omawiana na seminarium: tryb gracza i panel administratora ukończone,
44 kategorie i 440 pytań, testy jednostkowe 10/10 i integracyjne 31/31.

## Znane ograniczenia

- Połączenie przez HTTP, nie HTTPS — hasło i token administratora nie są szyfrowane w sieci; tylko do sieci lokalnej.
- Baza na koncie `root` bez hasła (konfiguracja deweloperska XAMPP), dane połączenia powtórzone w każdym pliku PHP.
- Adres serwera wpisany w kodzie (`ApiClient.BASE_URL`) — zmiana wymaga przebudowania aplikacji.
- Token sesji administratora nie wygasa po czasie (kasuje go wylogowanie lub nowe logowanie).
- Wyniki graczy są zapisywane w bazie, ale aplikacja nie ma ekranu rankingu.
- Brak automatycznych testów interfejsu; testy ręczne tylko na jednym telefonie (Samsung Galaxy S24+).

---

*Autor: Damian Robaczewski*
