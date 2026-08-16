<?php
error_reporting(0);
ini_set('display_errors', 0);
header("Content-Type: application/json; charset=UTF-8");

$conn = new mysqli("localhost", "root", "", "quizdb");

if ($conn->connect_error) {
    die(json_encode(["error" => "Błąd połączenia z bazą"]));
}

$category_id = isset($_GET['category_id']) ? intval($_GET['category_id']) : null;

if (!$category_id) {
    echo json_encode([]);
    exit;
}

$stmt = $conn->prepare(
    "SELECT id, category_id, question_text, answerA, answerB, answerC, answerD, correct_answer 
     FROM questions 
     WHERE category_id = ? 
     ORDER BY id DESC"
);
$stmt->bind_param("i", $category_id);
$stmt->execute();
$result = $stmt->get_result();

$questions = [];
while ($row = $result->fetch_assoc()) {
    $questions[] = $row;
}

echo json_encode($questions);

$stmt->close();
$conn->close();
