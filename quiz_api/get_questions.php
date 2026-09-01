<?php
error_reporting(0);
ini_set('display_errors', 0);
header("Content-Type: application/json");

$conn = new mysqli("127.0.0.1", "root", "", "quizdb");

$category_id = intval($_GET['category_id']);
$limit = intval($_GET['limit']);

$sql = "SELECT * FROM questions
        WHERE category_id = $category_id
        ORDER BY RAND()
        LIMIT $limit";

$result = $conn->query($sql);

$questions = [];

while ($row = $result->fetch_assoc()) {
    $questions[] = $row;
}

echo json_encode($questions);
