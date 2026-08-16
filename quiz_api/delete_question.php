<?php
error_reporting(0);
ini_set('display_errors', 0);
header("Content-Type: application/json; charset=UTF-8");

$conn = new mysqli("localhost", "root", "", "quizdb");

if ($conn->connect_error) {
    echo json_encode(["status" => "error", "message" => "Błąd połączenia"]);
    exit;
}

$id = isset($_POST['id']) ? intval($_POST['id']) : 0;

if ($id <= 0) {
    echo json_encode(["status" => "error", "message" => "Podaj ID pytania"]);
    exit;
}

$sql = "DELETE FROM questions WHERE id = $id";

if ($conn->query($sql)) {
    echo json_encode(["status" => "ok", "message" => "Pytanie usunięte"]);
} else {
    echo json_encode(["status" => "error", "message" => "Błąd usuwania"]);
}

$conn->close();