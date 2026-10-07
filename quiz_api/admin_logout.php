<?php
error_reporting(0);
ini_set('display_errors', 0);
header("Content-Type: application/json; charset=UTF-8");

$conn = new mysqli("127.0.0.1", "root", "", "quizdb");

if ($conn->connect_error) {
    echo json_encode(["status" => "error", "message" => "Błąd połączenia"]);
    exit;
}

require 'auth.php';

// wylogowanie = skasowanie tokenu, po którym stary token przestaje działać
$stmt = $conn->prepare("UPDATE admins SET token = NULL WHERE token = ?");
$stmt->bind_param("s", $token);
$stmt->execute();

echo json_encode(["status" => "ok", "message" => "Wylogowano"]);

$stmt->close();
$conn->close();
