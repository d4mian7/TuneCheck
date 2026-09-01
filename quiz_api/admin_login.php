<?php
error_reporting(0);
ini_set('display_errors', 0);
header("Content-Type: application/json; charset=UTF-8");

$conn = new mysqli("127.0.0.1", "root", "", "quizdb");

if ($conn->connect_error) {
    echo json_encode(["status" => "error", "message" => "Błąd połączenia"]);
    exit;
}

$username = $_POST['username'] ?? null;
$password = $_POST['password'] ?? null;

if (!$username || !$password) {
    echo json_encode(["status" => "error", "message" => "Podaj login i hasło"]);
    exit;
}

$stmt = $conn->prepare("SELECT id, password FROM admins WHERE username = ?");
$stmt->bind_param("s", $username);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 0) {
    echo json_encode(["status" => "error", "message" => "Nieprawidłowy login lub hasło"]);
    $stmt->close();
    $conn->close();
    exit;
}

$admin = $result->fetch_assoc();

if (password_verify($password, $admin['password'])) {
    echo json_encode([
        "status" => "ok",
        "admin_id" => $admin['id'],
        "message" => "Zalogowano pomyślnie"
    ]);
} else {
    echo json_encode(["status" => "error", "message" => "Nieprawidłowy login lub hasło"]);
}

$stmt->close();
$conn->close();
