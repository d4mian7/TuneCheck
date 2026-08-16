<?php
error_reporting(0);
ini_set('display_errors', 0);
header("Content-Type: application/json; charset=UTF-8");

$conn = new mysqli("localhost", "root", "", "quizdb");

if ($conn->connect_error) {
    echo json_encode(["status" => "error", "message" => "Błąd połączenia"]);
    exit;
}

$category_id = $_POST['category_id'] ?? null;
$question_text = $_POST['question_text'] ?? null;
$answerA = $_POST['answerA'] ?? null;
$answerB = $_POST['answerB'] ?? null;
$answerC = $_POST['answerC'] ?? null;
$answerD = $_POST['answerD'] ?? null;
$correct_answer = $_POST['correct_answer'] ?? null;

if (!$category_id || !$question_text || !$answerA || !$answerB || !$answerC || !$answerD || !$correct_answer) {
    echo json_encode(["status" => "error", "message" => "Wszystkie pola są wymagane"]);
    exit;
}

// Walidacja correct_answer
if (!in_array($correct_answer, ["A", "B", "C", "D"])) {
    echo json_encode(["status" => "error", "message" => "Poprawna odpowiedź musi być A, B, C lub D"]);
    exit;
}

$stmt = $conn->prepare(
    "INSERT INTO questions (category_id, question_text, answerA, answerB, answerC, answerD, correct_answer) 
     VALUES (?, ?, ?, ?, ?, ?, ?)"
);
$stmt->bind_param("issssss", $category_id, $question_text, $answerA, $answerB, $answerC, $answerD, $correct_answer);

if ($stmt->execute()) {
    echo json_encode([
        "status" => "ok",
        "id" => $stmt->insert_id,
        "message" => "Pytanie dodane"
    ]);
} else {
    echo json_encode(["status" => "error", "message" => "Błąd dodawania pytania"]);
}

$stmt->close();
$conn->close();
