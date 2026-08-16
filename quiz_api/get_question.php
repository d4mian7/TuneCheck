<?php
error_reporting(0);
ini_set('display_errors', 0);
header("Content-Type: application/json; charset=UTF-8");

$conn = new mysqli("localhost", "root", "", "quizdb");

if ($conn->connect_error) {
    echo json_encode(["status" => "error", "message" => "Błąd połączenia"]);
    exit;
}

$id = $_POST['id'] ?? null;

if (!$id) {
    echo json_encode(["status" => "error", "message" => "Podaj ID pytania"]);
    exit;
}

$id = intval($id);

$stmt = $conn->prepare("DELETE FROM questions WHERE id = ?");
$stmt->bind_param("i", $id);

if ($stmt->execute()) {
    echo json_encode(["status" => "ok", "message" => "Pytanie usunięte"]);
} else {
    echo json_encode(["status" => "error", "message" => "Błąd usuwania"]);
}

$stmt->close();
$conn->close();