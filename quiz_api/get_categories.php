<?php
error_reporting(0);
ini_set('display_errors', 0);
header("Content-Type: application/json; charset=UTF-8");

$conn = new mysqli("127.0.0.1", "root", "", "quizdb");

if ($conn->connect_error) {
    die(json_encode(["error" => "Błąd połączenia z bazą"]));
}

// najpierw kategorie tematyczne w ustalonej kolejności (theme_order), potem artyści alfabetycznie
$result = $conn->query("SELECT id, name FROM categories ORDER BY theme_order IS NULL, theme_order, name");

$categories = [];

while ($row = $result->fetch_assoc()) {
    $categories[] = $row;
}

echo json_encode($categories);
$conn->close();
