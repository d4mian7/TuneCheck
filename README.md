# TuneCheck 🎵

Mobilna aplikacja quizowa o tematyce muzycznej — **projekt inżynierski** (Informatyka, Uniwersytet Kazimierza Wielkiego w Bydgoszczy).

Architektura **klient–serwer**: natywna aplikacja Android (Kotlin) + backend PHP + relacyjna baza MySQL.

## Funkcje

**Tryb gracza**
- wybór kategorii pobieranych dynamicznie z API,
- rozgrywka: 5 losowych pytań na kategorię, 4 odpowiedzi, natychmiastowy feedback (zielony/czerwony), pasek postępu,
- ekran wyniku z zapisem rezultatu do bazy i rankingiem.

**Tryb administratora** (wbudowany w aplikację — zarządzanie treścią bez dotykania bazy)
- logowanie administratora (weryfikacja hasła **bcrypt** po stronie backendu),
- CRUD kategorii i pytań z poziomu telefonu.

## Architektura

```
[Android / Kotlin]  --HTTP/JSON-->  [PHP: quiz_api/]  --SQL-->  [MySQL: quizdb]
```

- **Klient:** architektura oparta na Activity; sieć przez OkHttp (singleton `ApiClient`, wzorzec enqueue + `runOnUiThread`), animacje przejść między ekranami, Material 3 (tryb ciemny).
- **Backend:** `quiz_api/` — endpoint-per-file (PHP + mysqli, prepared statements), auth admina przez `password_verify` (bcrypt).
- **Baza:** MySQL (`quizdb`) — kategorie, pytania, wyniki, administratorzy.

Kluczowe pliki klienta: `MainActivity` (start + logowanie admina) · `CategoryActivity` · `QuizActivity` (logika rozgrywki) · `ResultActivity` · `AdminPanelActivity` + ekrany CRUD.

## Uruchomienie

1. Backend: XAMPP (Apache + MySQL) → katalog `quiz_api/` do `htdocs/`, baza `quizdb` (MySQL, lokalnie `root` bez hasła — konfiguracja deweloperska).
2. Aplikacja: Android Studio → emulator łączy się z backendem przez `http://10.0.2.2/quiz_api/`.

```bash
./gradlew assembleDebug   # build
./gradlew test            # testy jednostkowe
```

Min SDK 26 · Target SDK 36 · Java 11.

## Status

Część praktyczna pracy inżynierskiej — ukończona w ~90% (stan: czerwiec 2026). W ramach rozbudowy uzgodnionej z promotorem dodano panel administratora wbudowany w aplikację.

---

*Autor: Damian Robaczewski*
