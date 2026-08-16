<?php
error_reporting(0);
ini_set('display_errors', 0);
header("Content-Type: application/json; charset=UTF-8");

$conn = new mysqli("localhost", "root", "", "quizdb");

if ($conn->connect_error) {
    echo json_encode(["status" => "error", "message" => "Błąd połączenia"]);
    exit;
}

$name = $_POST['name'] ?? null;

if (!$name || trim($name) === "") {
    echo json_encode(["status" => "error", "message" => "Podaj nazwę kategorii"]);
    exit;
}

$name = trim($name);

$stmt = $conn->prepare("INSERT INTO categories (name) VALUES (?)");
$stmt->bind_param("s", $name);

if ($stmt->execute()) {
    echo json_encode([
        "status" => "ok",
        "id" => $stmt->insert_id,
        "message" => "Kategoria dodana"
    ]);
} else {
    echo json_encode(["status" => "error", "message" => "Błąd dodawania kategorii"]);
}

$stmt->close();
$conn->close();
