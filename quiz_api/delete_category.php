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
    echo json_encode(["status" => "error", "message" => "Podaj ID kategorii"]);
    exit;
}

$conn->query("DELETE FROM questions WHERE category_id = $id");
$conn->query("DELETE FROM scores WHERE category_id = $id");
$conn->query("DELETE FROM categories WHERE id = $id");

echo json_encode(["status" => "ok", "message" => "Kategoria usunięta"]);

$conn->close();