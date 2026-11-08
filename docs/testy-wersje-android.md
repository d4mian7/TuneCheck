# Testy na różnych wersjach Androida (emulatory)

Data: 08.11.2026 · wersja aplikacji: `v1.0` (commit 9015d18) · backend: XAMPP na komputerze (`BASE_URL` `http://192.168.1.4/quiz_api/`)
Testy ręczne, ten sam zestaw 8 scenariuszy na każdym urządzeniu. Zrzuty ekranu: `zrzuty/api26_*`, `api34_*`, `api36_*`.

## Środowisko testowe

| Urządzenie | Android (API) | Ekran | Nawigacja systemowa | Obraz systemu |
|---|---|---|---|---|
| Emulator `Test_API26` (profil Pixel) | 8.0 (26), minSdk | 1080×1920, 5,0" (16:9) | 3 przyciski | Google APIs, x86 |
| Emulator `Pixel_7` | 14 (34) | 1080×2400 (20:9) | gesty | Google Play, x86_64 |
| Emulator `Test_API36` (profil Pixel) | 16 (36), targetSdk | 1080×1920, 5,0" (16:9) | gesty | Google Play, x86_64 |
| Samsung Galaxy S24+ (fizyczny) | 17 | 1440×3120 | gesty | One UI |

## Scenariusze i wyniki

| # | Scenariusz | Oczekiwany wynik | API 26 | API 34 | API 36 |
|---|---|---|---|---|---|
| 1 | Nick → ZAGRAJ | lista kategorii z serwera | OK | OK | OK |
| 2 | Wybór kategorii | odliczanie 3-2-1, pytanie 1/5 | OK | OK | OK |
| 3 | Dobra odp. / zła odp. / brak odp. 10 s | zielony / czerwony + poprawna / automatyczne przejście | OK | OK | OK |
| 4 | Koniec quizu → ZAPISZ WYNIK | „Wynik zapisany”, przycisk „ZAPISANO” | OK | OK | OK |
| 5 | ZAGRAJ bez nicku | komunikat „Podaj nazwę użytkownika” | OK* | OK | OK |
| 6 | Logowanie admina złym hasłem | „Nieprawidłowy login lub hasło” | OK | OK | OK |
| 7 | Logowanie admina → dodanie i usunięcie pytania | pytanie pojawia się i znika z listy | OK | OK | OK |
| 8 | Wylogowanie, przycisk Wstecz, obrót ekranu | brak awarii, czytelny układ | OK* | OK | OK |

\* funkcja działa, drobne różnice w układzie — opis niżej.

## Zaobserwowane różnice w wyglądzie

1. **Ikona kłódki częściowo zasłania logo — tylko API 26** (`api26_komunikat.png`).
   Na tym samym ekranie 1080×1920 z API 36 problem nie występuje (`api36_komunikat.png`), więc przyczyną nie jest sam rozmiar ekranu.
   Na Androidzie 8 aplikacja rysuje się pod paskiem stanu (mniej miejsca w pionie), a na Androidzie 15+ aplikacje z targetSdk ≥ 35 rysują się od krawędzi do krawędzi (edge-to-edge), także pod paskiem stanu.
   Na API 34 aplikacja też jest pod paskiem stanu, ale ekran 20:9 jest wyższy, więc miejsca wystarcza.
2. **Komunikat (AppToast) nachodzi na pasek nawigacji — tylko API 26.**
   `AppToast` ustawia komunikat 32 dp od dołu okna, bez uwzględnienia wysokości paska nawigacji. Przy nawigacji trzyprzyciskowej (wyższy pasek) komunikat zachodzi na pasek; przy nawigacji gestami (API 34, 36, S24+) mieści się nad nim.

## Wnioski

- Wszystkie funkcje działają na najstarszej wspieranej (API 26) i najnowszej docelowej (API 36) wersji Androida oraz na wersji pośredniej (API 34).
- Różnice dotyczą wyłącznie układu na starszym systemie z niskim ekranem i nawigacją trzyprzyciskową.
- Kierunek rozwoju: uwzględnić wcięcia systemowe (`WindowInsets` — pasek stanu i nawigacji) przy pozycjonowaniu kłódki i komunikatu.
