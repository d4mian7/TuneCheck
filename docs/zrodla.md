# Źródła — TuneCheck (praca inżynierska)

Zbiór źródeł do bibliografii pracy. Daty dostępu to dni, w których dane źródło zostało faktycznie otwarte.
Format wpisu: autor/tytuł, wydawca, adres, data dostępu.

## 1. Dokumentacja technologii

| Element projektu | Wersja w projekcie | Źródło |
|---|---|---|
| Android SDK (compile/target 36, min 26) | Android Gradle Plugin 8.13.2 | Android Developers, „Developer guides”, https://developer.android.com/guide [dostęp: 04.10.2026] |
| Aktywności (architektura ekranów) | — | Android Developers, „Introduction to activities”, https://developer.android.com/guide/components/activities/intro-activities [dostęp: 04.10.2026] |
| Kotlin | 2.0.21 | JetBrains, „Kotlin Docs”, https://kotlinlang.org/docs/home.html [dostęp: 04.10.2026] |
| OkHttp (komunikacja HTTP z serwerem) | 4.12.0 | Square, „OkHttp”, https://github.com/square/okhttp [dostęp: 04.10.2026] |
| Format wymiany danych JSON | — | „Wprowadzenie do JSON”, https://www.json.org/json-pl.html [dostęp: 04.10.2026] |
| CountDownTimer (licznik czasu pytania) | — | Android Developers, „CountDownTimer”, https://developer.android.com/reference/android/os/CountDownTimer [dostęp: 04.10.2026] |
| GridLayout (siatka kategorii) | — | Android Developers, „GridLayout”, https://developer.android.com/reference/android/widget/GridLayout [dostęp: 04.10.2026] |
| Ikona adaptacyjna aplikacji | — | Android Developers, „Adaptive icons”, https://developer.android.com/develop/ui/views/launch/icon_design_adaptive [dostęp: 04.10.2026] |
| Material Design 3 (motyw aplikacji) | Material Components 1.13.0 | Google, „Material Design 3”, https://m3.material.io/ [dostęp: 04.10.2026] |
| PHP (endpointy API) | 8.2.12 | The PHP Group, „MySQL Improved Extension (mysqli)”, https://www.php.net/manual/en/book.mysqli.php [dostęp: 04.10.2026] |
| Hasło admina (bcrypt) | — | The PHP Group, „password_hash”, https://www.php.net/manual/en/function.password-hash.php [dostęp: 04.10.2026] |
| Weryfikacja hasła admina | — | The PHP Group, „password_verify”, https://www.php.net/manual/en/function.password-verify.php [dostęp: 04.10.2026] |
| Baza danych | MariaDB 10.4.32 | MariaDB Foundation, „MariaDB Documentation”, https://mariadb.com/docs/ [dostęp: 04.10.2026] |
| Serwer WWW | Apache 2.4.58 | Apache Software Foundation, „Apache HTTP Server Version 2.4 Documentation”, https://httpd.apache.org/docs/2.4/ [dostęp: 04.10.2026] |
| Środowisko serwerowe | XAMPP | Apache Friends, „XAMPP”, https://www.apachefriends.org/ [dostęp: 04.10.2026] |

## 2. Czcionki i grafiki

| Zasób | Pochodzenie | Licencja / źródło |
|---|---|---|
| Czcionka Manrope (`res/font/manrope_*.ttf`) | Google Fonts | SIL Open Font License 1.1 — https://fonts.google.com/specimen/Manrope, https://openfontlicense.org/ [dostęp: 04.10.2026] |
| Czcionka Space Grotesk (`res/font/space_grotesk_*.ttf`) | Google Fonts | SIL Open Font License 1.1 — https://fonts.google.com/specimen/Space+Grotesk [dostęp: 04.10.2026] |
| Tło ekranu startowego `res/drawable/bg_start.png` | Grafika wygenerowana przez autora pracy w ChatGPT (OpenAI) — własne prompty, dopracowywana iteracyjnie metodą prób i błędów | OpenAI, ChatGPT, https://chatgpt.com/ |
| Tło pozostałych ekranów `res/drawable/bg_rap.png` | Grafika wygenerowana przez autora pracy w ChatGPT (OpenAI) — własne prompty, dopracowywana iteracyjnie metodą prób i błędów | OpenAI, ChatGPT, https://chatgpt.com/ |
| Ikona aplikacji (`res/drawable-*dpi/ic_launcher_foreground.png`) | Słuchawki wycięte z `bg_start.png` algorytmem GrabCut (OpenCV) | C. Rother, V. Kolmogorov, A. Blake, „GrabCut: Interactive Foreground Extraction using Iterated Graph Cuts”, ACM SIGGRAPH 2004; OpenCV, „Interactive Foreground Extraction using GrabCut Algorithm”, https://docs.opencv.org/4.x/d8/d83/tutorial_py_grabcut.html [dostęp: 04.10.2026] |
| Ikony wektorowe (`res/drawable/ic_*.xml`) | Narysowane na potrzeby projektu (wektory Android) przy pomocy asystenta AI Claude Code | Anthropic, Claude Code, https://claude.com/claude-code |

## 3. Źródła treści pytań quizu

Treść pytań (440 pytań w 44 kategoriach) została zweryfikowana w ogólnodostępnych źródłach internetowych,
głównie w anglojęzycznej Wikipedii oraz serwisach muzycznych (Billboard, Complex, XXL, Rolling Stone, Rap-Up, Revolt, AllMusic).

**Pierwsze pytania** — 20 pytań w 4 kategoriach (A$AP Rocky, Future, Lil Uzi Vert, Playboi Carti) — autor pracy
ułożył samodzielnie na podstawie własnej wiedzy, informacji o artystach w aplikacji Spotify oraz artykułów
o poszczególnych artystach w Wikipedii. Podczas audytu 29.09.2026 zostały one zweryfikowane w źródłach
wymienionych niżej (część przeredagowano lub zmieniono kolejność odpowiedzi, aby poprawne odpowiedzi
rozkładały się równo między A–D). Pozostałe pytania powstały później i zostały zweryfikowane w tych samych źródłach.

- Spotify AB, aplikacja Spotify (profile artystów), https://open.spotify.com/ [dostęp: wrzesień 2026]

Proponowane zdanie do pracy (rozdział o bazie danych):
> Pierwsze pytania autor opracował na podstawie własnej wiedzy, profili artystów w serwisie Spotify oraz
> Wikipedii. Całość bazy pytań zweryfikowano w ogólnodostępnych źródłach internetowych, głównie anglojęzycznej
> Wikipedii oraz serwisach muzycznych (m.in. Billboard, Complex, XXL, Rolling Stone), stan na wrzesień–październik 2026 r.

Kategorie tematyczne (Lata 2020./2010./2000./90., Alter ego i ksywki, Beefy i konflikty, Debiutanckie albumy,
Featy i kolaboracje) zawierają wyłącznie pytania o artystów, którzy mają własne kategorie — ich źródła są
w sekcjach poszczególnych artystów poniżej.

### 21 Savage

- „21 Savage”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/21_Savage [dostęp: 29.09.2026]

### 2Pac

- „Toss It Up”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Toss_It_Up [dostęp: 29.09.2026]
- „Today In Hip Hop History: 2Pac’s Epic Diss Track “Hit ‘Em Up” Dropped 28 Years Ago”, thesource.com, https://thesource.com/2024/06/04/today-in-hip-hop-history-2pacs-epic-diss-track-hit-em-up-dropped-28-years-ago/ [dostęp: 29.09.2026]
- „Nas recalls confronting Tupac about ‘Makaveli’ diss: “He thought I was dissing him””, revolt.tv, https://revolt.tv/news/2020/8/21/21396005/nas-recalls-confronting-tupac-makaveli-diss [dostęp: 29.09.2026]
- Artykuł w serwisie complex.com, https://www.complex.com/music/nas-disses-2pac-unreleased-track-surfaced [dostęp: 29.09.2026]
- „Tupac Shakur”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Tupac_Shakur [dostęp: 29.09.2026]
- „Greatest Hits (Tupac Shakur album)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Greatest_Hits_(Tupac_Shakur_album) [dostęp: 29.09.2026]
- „Changes (Tupac Shakur song)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Changes_(Tupac_Shakur_song) [dostęp: 29.09.2026]

### 50 Cent

- „50 Cent”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/50_Cent [dostęp: 29.09.2026]
- „XXL celebrates 50 years of hip-hop with this moment:”, xxlmag.com, https://www.xxlmag.com/today-in-hip-hop-50-cent-the-game-beef-hot-97-shooting/ [dostęp: 29.09.2026]
- „50 cent beefing g unit members history timeline”, complex.com, https://www.complex.com/music/a/brad-callas/50-cent-beefing-g-unit-members-history-timeline [dostęp: 29.09.2026]

### A$AP Rocky

- „ASAP Rocky”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/ASAP_Rocky [dostęp: 29.09.2026]

### Chief Keef

- „Almighty So”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Almighty_So [dostęp: 29.09.2026]
- Artykuł w serwisie complex.com, https://www.complex.com/music/2013/10/chief-keef-almighty-so-mixtape [dostęp: 29.09.2026]
- „Chief Keef”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Chief_Keef [dostęp: 29.09.2026]

### Destroy Lonely

- „Destroy Lonely”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Destroy_Lonely [dostęp: 29.09.2026]
- „Destroy Lonely”, allmusic.com, https://www.allmusic.com/artist/mn0004270428 [dostęp: 29.09.2026]
- „Love Lasts Forever (album)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Love_Lasts_Forever_(album) [dostęp: 29.09.2026]
- „LOVE LASTS FOREVER”, allmusic.com, https://www.allmusic.com/album/love-lasts-forever-mw0004363926 [dostęp: 29.09.2026]
- „Opium (record label)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Opium_(record_label) [dostęp: 29.09.2026]

### Don Toliver

- „Private Landing”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Private_Landing [dostęp: 29.09.2026]
- Artykuł w serwisie au.rollingstone.com, https://au.rollingstone.com/music/music-news/don-toliver-justin-bieber-private-landing-performance-los-angeles-68046 [dostęp: 29.09.2026]
- „Don Toliver discography”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Don_Toliver_discography [dostęp: 29.09.2026]
- Artykuł w serwisie revolt.tv, https://www.revolt.tv/article/2023-02-17/274156/don-toliver-4-me-kali-uchis-song [dostęp: 29.09.2026]
- „Brother Stone”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Brother_Stone [dostęp: 29.09.2026]
- „Kryptonite (Don Toliver song)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Kryptonite_(Don_Toliver_song) [dostęp: 29.09.2026]

### Dr. Dre

- „Dr. Dre”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Dr._Dre [dostęp: 29.09.2026]

### Drake

- „Drake (musician)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Drake_(musician) [dostęp: 29.09.2026]
- „Watch Drake’s Million-Dollar Donation Spree in ‘God’s Plan’ Video”, rollingstone.com, https://www.rollingstone.com/music/music-news/watch-drakes-million-dollar-donation-spree-in-gods-plan-video-197171/ [dostęp: 29.09.2026]
- „Forever (Drake song)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Forever_(Drake_song) [dostęp: 29.09.2026]
- „More than a Game (soundtrack)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/More_than_a_Game_(soundtrack) [dostęp: 29.09.2026]
- „Forever (Drake, Kanye West, Lil Wayne, and Eminem song)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Forever_(Drake,_Kanye_West,_Lil_Wayne,_and_Eminem_song) [dostęp: 29.09.2026]
- „Honestly, Nevermind”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Honestly,_Nevermind [dostęp: 29.09.2026]

### Eminem

- „The Eminem Show”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/The_Eminem_Show [dostęp: 29.09.2026]
- „Eminem”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Eminem [dostęp: 29.09.2026]

### Future

- „Future (rapper)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Future_(rapper) [dostęp: 29.09.2026]

### Gunna

- „Gunna (rapper)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Gunna_(rapper) [dostęp: 29.09.2026]

### Ice Cube

- „Ice Cube”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Ice_Cube [dostęp: 29.09.2026]

### Jay-Z

- „Jay-Z”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Jay-Z [dostęp: 29.09.2026]
- „Vol. 2... Hard Knock Life”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Vol._2..._Hard_Knock_Life [dostęp: 29.09.2026]

### Juice WRLD

- „Lucid Dreams (Juice Wrld song)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Lucid_Dreams_(Juice_Wrld_song) [dostęp: 29.09.2026]
- „All Girls Are the Same”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/All_Girls_Are_the_Same [dostęp: 29.09.2026]
- „Juice Wrld”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Juice_Wrld [dostęp: 29.09.2026]

### Kanye West

- „Kanye West”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Kanye_West [dostęp: 29.09.2026]

### Ken Carson

- „More Chaos”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/More_Chaos [dostęp: 29.09.2026]
- „More Chaos”, allmusic.com, https://www.allmusic.com/album/more-chaos-mw0004502921 [dostęp: 29.09.2026]
- „Ken Carson”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Ken_Carson [dostęp: 29.09.2026]

### Kendrick Lamar

- „Kendrick Lamar”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Kendrick_Lamar [dostęp: 29.09.2026]
- „Good Kid, M.A.A.D City”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Good_Kid,_M.A.A.D_City [dostęp: 29.09.2026]
- „Kendrick Lamar to Perform at 2025 Super Bowl Halftime Show”, au.variety.com, https://au.variety.com/2024/music/global/super-bowl-2025-halftime-performer-17458 [dostęp: 29.09.2026]

### Kid Cudi

- „Entergalactic (album)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Entergalactic_(album) [dostęp: 29.09.2026]
- „Entergalactic (TV special)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Entergalactic_(TV_special) [dostęp: 29.09.2026]
- „Kid Cudi”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Kid_Cudi [dostęp: 29.09.2026]

### Lil Baby

- „WHAM (album)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/WHAM_(album) [dostęp: 29.09.2026]
- Artykuł w serwisie rap-up.com, https://rap-up.com/article/lil-baby-announces-release-date-for-wham-out-january-3-2025 [dostęp: 29.09.2026]
- „Lil Baby”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Lil_Baby [dostęp: 29.09.2026]

### Lil Uzi Vert

- „Aye (Lil Uzi Vert song)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Aye_(Lil_Uzi_Vert_song) [dostęp: 29.09.2026]
- Artykuł w serwisie xxlmag.com, https://www.xxlmag.com/lil-uzi-vert-pink-tape-album/ [dostęp: 29.09.2026]
- „Lil Uzi Vert”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Lil_Uzi_Vert [dostęp: 29.09.2026]
- „Go Off (Lil Uzi Vert, Quavo and Travis Scott song)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Go_Off_(Lil_Uzi_Vert,_Quavo_and_Travis_Scott_song) [dostęp: 29.09.2026]
- „The Fate of the Furious (soundtrack)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/The_Fate_of_the_Furious_(soundtrack) [dostęp: 29.09.2026]

### Lil Wayne

- „Lil Wayne”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Lil_Wayne [dostęp: 29.09.2026]
- „Tha Block Is Hot”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Tha_Block_Is_Hot [dostęp: 29.09.2026]

### Metro Boomin

- „Metro Boomin”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Metro_Boomin [dostęp: 29.09.2026]
- „Space Cadet (song)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Space_Cadet_(song) [dostęp: 29.09.2026]
- „Not All Heroes Wear Capes”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Not_All_Heroes_Wear_Capes [dostęp: 29.09.2026]

### Migos

- „Migos”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Migos [dostęp: 29.09.2026]
- Artykuł w serwisie xxlmag.com, https://www.xxlmag.com/cardi-b-offset-married/ [dostęp: 29.09.2026]

### N.W.A

- „N.W.A”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/N.W.A [dostęp: 29.09.2026]

### Nas

- Artykuł w serwisie revolt.tv, https://www.revolt.tv/article/nas-dj-premier-release-light-years-album [dostęp: 29.09.2026]
- „Nas”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Nas [dostęp: 29.09.2026]

### OutKast

- „Idlewild (film)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Idlewild_(film) [dostęp: 29.09.2026]
- „Outkast”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Outkast [dostęp: 29.09.2026]
- „New Blue Sun”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/New_Blue_Sun [dostęp: 29.09.2026]

### Playboi Carti

- „Playboi Carti”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Playboi_Carti [dostęp: 29.09.2026]
- „Whole Lotta Red”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Whole_Lotta_Red [dostęp: 29.09.2026]
- „Die Lit”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Die_Lit [dostęp: 29.09.2026]

### Pop Smoke

- „Pop Smoke”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Pop_Smoke [dostęp: 29.09.2026]
- „Dior (Pop Smoke song)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Dior_(Pop_Smoke_song) [dostęp: 29.09.2026]
- „808Melo”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/808Melo [dostęp: 29.09.2026]

### Snoop Dogg

- „Snoop Dogg”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Snoop_Dogg [dostęp: 29.09.2026]
- „Gin and Juice”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Gin_and_Juice [dostęp: 29.09.2026]
- „Doggystyle”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Doggystyle [dostęp: 29.09.2026]

### The Notorious B.I.G.

- „The Notorious B.I.G.”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/The_Notorious_B.I.G. [dostęp: 29.09.2026]
- „Notorious (2009 film)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Notorious_(2009_film) [dostęp: 29.09.2026]
- „Notorious (2009)”, IMDb, https://www.imdb.com/title/tt0472198/ [dostęp: 29.09.2026]

### Travis Scott

- „Travis Scott”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Travis_Scott [dostęp: 29.09.2026]
- „The Scotts”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/The_Scotts [dostęp: 29.09.2026]
- Artykuł w serwisie complex.com, https://www.complex.com/music/2020/04/travis-scott-kid-cudi-the-scotts [dostęp: 29.09.2026]

### Trippie Redd

- „Trippie Redd”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Trippie_Redd [dostęp: 29.09.2026]

### Tyler, The Creator

- „Tyler, the Creator”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Tyler,_the_Creator [dostęp: 29.09.2026]

### Yeat

- „Yeat”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Yeat [dostęp: 04.10.2026]
- „2093 (album)”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/2093_(album) [dostęp: 04.10.2026]
- „Rich Minion”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Rich_Minion [dostęp: 04.10.2026]
- Artykuł w serwisie thefader.com, https://www.thefader.com/2022/06/29/yeat-rich-minion-minions-soundtrack [dostęp: 04.10.2026]
- „Lyfestyle”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Lyfestyle [dostęp: 04.10.2026]
- „Yeat Lands First No. 1 Album on Billboard 200 with ‘LYFESTYLE’”, thesource.com, https://thesource.com/?p=738026 [dostęp: 04.10.2026]
- „2 Alive”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/2_Alive [dostęp: 04.10.2026]

### Young Thug

- „Barter 6”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Barter_6 [dostęp: 29.09.2026]
- „Stream Young Thug’s New Album, ‘Barter 6,’ Right Now”, thesource.com, https://thesource.com/2015/04/16/stream-young-thugs-new-album-barter-6-right-now/ [dostęp: 29.09.2026]
- „Young Thug”, Wikipedia (wersja angielska), https://en.wikipedia.org/wiki/Young_Thug [dostęp: 29.09.2026]

## 4. Uwagi (nie do pracy — notatki robocze)

- Źródła w sekcji 3 to strony faktycznie otwarte lub wyniki wyszukiwania użyte przy weryfikacji pytań
  podczas audytu (29.09.2026) i przy dodaniu kategorii Yeat (04.10.2026).
- Nie ma przypisania „pytanie → konkretne źródło” dla każdego z 440 pytań — źródła są pogrupowane po artystach.
  Jeśli promotor będzie chciał załącznik z przypisaniem do każdego pytania, trzeba je uzupełnić.
- Od 04.10.2026 każde nowe pytanie dopisujemy tutaj od razu, z adresem i datą dostępu.
- Przy projekcie korzystano z narzędzi AI: ChatGPT (grafiki tła), Claude Code (kod, ikony wektorowe,
  weryfikacja pytań) — sprawdź regulamin uczelni/wydziału, czy wymagane jest oświadczenie o użyciu narzędzi AI.
