<?php
error_reporting(0);
ini_set('display_errors', 0);
header("Content-Type: application/json");

$conn = new mysqli("127.0.0.1", "root", "", "quizdb");

if ($conn->connect_error) {
    echo json_encode(["status" => "error", "message" => "Błąd połączenia"]);
    exit;
}

$username = $_POST['username'] ?? null;
$category_id = $_POST['category_id'] ?? null;
$score = $_POST['score'] ?? null;
$total = $_POST['total'] ?? null;

if (!$username || !$category_id || !$score || !$total) {
    echo json_encode(["status" => "error", "message" => "Brak danych"]);
    exit;
}

$stmt = $conn->prepare(
    "INSERT INTO scores (username, category_id, score, total) VALUES (?, ?, ?, ?)"
);
$stmt->bind_param("siii", $username, $category_id, $score, $total);

if ($stmt->execute()) {
    echo json_encode(["status" => "ok"]);
} else {
    echo json_encode(["status" => "error", "message" => "Błąd zapisu"]);
}

$stmt->close();
$conn->close();
