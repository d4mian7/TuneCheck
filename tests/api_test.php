<?php
// Testy integracyjne API TuneCheck: prawdziwe żądania HTTP do quiz_api/ + sprawdzenie w bazie danych,
// czy dane zostały zapisane / odczytane / usunięte. Wszystko, co test dodaje, jest na końcu usuwane.
// Wymaga działającego XAMPP (Apache + MySQL). Uruchomienie: C:\xampp\php\php.exe tests\api_test.php

const BASE = "http://127.0.0.1/quiz_api/";
const TEST_TOKEN = "test_integracyjny_token";
const TEST_USER = "__test_api__";
const TEST_CATEGORY = "__Kategoria testowa__";

$db = new mysqli("127.0.0.1", "root", "", "quizdb");
$db->set_charset("utf8mb4");
$passed = 0;
$failed = 0;

function request($endpoint, $post = null, $token = null) {
    $ch = curl_init(BASE . $endpoint);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    if ($post !== null) {
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($post));
    }
    if ($token !== null) {
        curl_setopt($ch, CURLOPT_HTTPHEADER, ["X-Admin-Token: $token"]);
    }
    $body = curl_exec($ch);
    curl_close($ch);
    return json_decode($body, true);
}

function check($name, $ok) {
    global $passed, $failed;
    echo ($ok ? "[ OK ]  " : "[BŁĄD]  ") . $name . PHP_EOL;
    $ok ? $passed++ : $failed++;
}

function dbValue($sql) {
    global $db;
    $row = $db->query($sql)->fetch_row();
    return $row ? $row[0] : null;
}

// sprzątanie także wtedy, gdy któryś test przerwie skrypt
$originalToken = dbValue("SELECT token FROM admins WHERE username = 'admin'");
register_shutdown_function(function () use ($db, $originalToken) {
    $db->query("DELETE FROM scores WHERE username = '" . TEST_USER . "'");
    $id = $db->query("SELECT id FROM categories WHERE name = '" . TEST_CATEGORY . "'")->fetch_row();
    if ($id) {
        $db->query("DELETE FROM questions WHERE category_id = $id[0]");
        $db->query("DELETE FROM categories WHERE id = $id[0]");
    }
    $stmt = $db->prepare("UPDATE admins SET token = ? WHERE username = 'admin'");
    $stmt->bind_param("s", $originalToken);
    $stmt->execute();
});

echo "=== Gracz: pobieranie danych ===" . PHP_EOL;

$categories = request("get_categories.php");
check("get_categories zwraca listę kategorii", is_array($categories) && count($categories) > 0);
check("każda kategoria ma id, name i theme_order",
    count(array_filter($categories, fn($c) => isset($c["id"], $c["name"]) && array_key_exists("theme_order", $c))) === count($categories));
check("kategorie tematyczne są przed artystami", $categories[0]["theme_order"] !== null && end($categories)["theme_order"] === null);

$categoryId = (int)$categories[0]["id"];
$questions = request("get_questions.php?category_id=$categoryId&limit=5");
check("get_questions zwraca maksymalnie 5 pytań", is_array($questions) && count($questions) > 0 && count($questions) <= 5);
check("pytania należą do wybranej kategorii", count(array_filter($questions, fn($q) => (int)$q["category_id"] === $categoryId)) === count($questions));
check("poprawna odpowiedź każdego pytania to A, B, C lub D",
    count(array_filter($questions, fn($q) => in_array($q["correct_answer"], ["A", "B", "C", "D"]))) === count($questions));
check("nieistniejąca kategoria daje pustą listę", request("get_questions.php?category_id=999999&limit=5") === []);

echo PHP_EOL . "=== Gracz: zapis wyniku (zapis i odczyt z bazy) ===" . PHP_EOL;

$r = request("save_score.php", ["username" => TEST_USER, "category_id" => $categoryId, "score" => 3, "total" => 5]);
check("save_score przyjmuje wynik 3/5", ($r["status"] ?? "") === "ok");
check("wynik 3/5 jest zapisany w tabeli scores",
    dbValue("SELECT COUNT(*) FROM scores WHERE username = '" . TEST_USER . "' AND score = 3 AND total = 5 AND category_id = $categoryId") == 1);

$r = request("save_score.php", ["username" => TEST_USER, "category_id" => $categoryId, "score" => 0, "total" => 5]);
check("save_score przyjmuje wynik 0/5", ($r["status"] ?? "") === "ok");
check("wynik 0/5 jest zapisany w tabeli scores",
    dbValue("SELECT COUNT(*) FROM scores WHERE username = '" . TEST_USER . "' AND score = 0") == 1);

$before = dbValue("SELECT COUNT(*) FROM scores");
$r = request("save_score.php", ["username" => TEST_USER, "category_id" => $categoryId, "total" => 5]);
check("save_score odrzuca niepełne dane", ($r["status"] ?? "") === "error");
check("niepełne dane nie trafiają do bazy", dbValue("SELECT COUNT(*) FROM scores") == $before);

echo PHP_EOL . "=== Bezpieczeństwo ===" . PHP_EOL;

$r = request("admin_login.php", ["username" => "admin", "password" => "zle_haslo"]);
check("logowanie złym hasłem jest odrzucone", ($r["status"] ?? "") === "error" && !isset($r["token"]));
$r = request("admin_login.php", ["username" => "' OR '1'='1", "password" => "' OR '1'='1"]);
check("próba SQL injection w logowaniu jest odrzucona", ($r["status"] ?? "") === "error");
$injected = request("get_questions.php?category_id=" . urlencode("$categoryId OR 1=1") . "&limit=1000");
check("SQL injection w get_questions nie zwraca pytań z innych kategorii",
    is_array($injected) && count(array_filter($injected, fn($q) => (int)$q["category_id"] !== $categoryId)) === 0);
$r = request("add_category.php", ["name" => TEST_CATEGORY]);
check("dodanie kategorii bez tokenu jest odrzucone", ($r["message"] ?? "") === "Brak uprawnień");
$r = request("delete_question.php", ["id" => 1], "zly_token");
check("usunięcie pytania ze złym tokenem jest odrzucone", ($r["message"] ?? "") === "Brak uprawnień");
check("kategoria nie została dodana bez uprawnień", dbValue("SELECT COUNT(*) FROM categories WHERE name = '" . TEST_CATEGORY . "'") == 0);

echo PHP_EOL . "=== Administrator: operacje z tokenem (zapis, odczyt, usuwanie) ===" . PHP_EOL;

// token testowy wpisany bezpośrednio do bazy - test nie zna prawdziwego hasła administratora
$db->query("UPDATE admins SET token = '" . TEST_TOKEN . "' WHERE username = 'admin'");

$r = request("add_category.php", ["name" => TEST_CATEGORY], TEST_TOKEN);
check("dodanie kategorii z tokenem", ($r["status"] ?? "") === "ok");
$newCategoryId = (int)($r["id"] ?? 0);
check("nowa kategoria jest w bazie", dbValue("SELECT name FROM categories WHERE id = $newCategoryId") === TEST_CATEGORY);

$r = request("add_question.php", [
    "category_id" => $newCategoryId, "question_text" => "Pytanie testowe?",
    "answerA" => "A1", "answerB" => "B1", "answerC" => "C1", "answerD" => "D1", "correct_answer" => "B",
], TEST_TOKEN);
check("dodanie pytania z tokenem", ($r["status"] ?? "") === "ok");
$newQuestionId = (int)($r["id"] ?? 0);
check("nowe pytanie jest w bazie z poprawną odpowiedzią B", dbValue("SELECT correct_answer FROM questions WHERE id = $newQuestionId") === "B");

$r = request("add_question.php", [
    "category_id" => $newCategoryId, "question_text" => "Złe pytanie?",
    "answerA" => "A", "answerB" => "B", "answerC" => "C", "answerD" => "D", "correct_answer" => "E",
], TEST_TOKEN);
check("pytanie z poprawną odpowiedzią spoza A-D jest odrzucone", ($r["status"] ?? "") === "error");

$list = request("get_questions_admin.php?category_id=$newCategoryId", null, TEST_TOKEN);
check("lista pytań admina zawiera dodane pytanie", is_array($list) && count($list) === 1 && (int)$list[0]["id"] === $newQuestionId);

$r = request("delete_question.php", ["id" => $newQuestionId], TEST_TOKEN);
check("usunięcie pytania z tokenem", ($r["status"] ?? "") === "ok");
check("pytanie zniknęło z bazy", dbValue("SELECT COUNT(*) FROM questions WHERE id = $newQuestionId") == 0);

$r = request("delete_category.php", ["id" => $newCategoryId], TEST_TOKEN);
check("usunięcie kategorii z tokenem", ($r["status"] ?? "") === "ok");
check("kategoria zniknęła z bazy", dbValue("SELECT COUNT(*) FROM categories WHERE id = $newCategoryId") == 0);

$r = request("admin_logout.php", [], TEST_TOKEN);
check("wylogowanie kasuje token w bazie", ($r["status"] ?? "") === "ok" && dbValue("SELECT token FROM admins WHERE username = 'admin'") === null);
$r = request("add_category.php", ["name" => TEST_CATEGORY], TEST_TOKEN);
check("stary token po wylogowaniu jest odrzucony", ($r["message"] ?? "") === "Brak uprawnień");

echo PHP_EOL . "Wynik: $passed z " . ($passed + $failed) . " testów zaliczonych" . ($failed ? ", $failed niezaliczonych" : "") . PHP_EOL;
exit($failed ? 1 : 0);
