<?php
// Sprawdzenie uprawnień administratora - dołączane (require) w endpointach panelu admina,
// po utworzeniu połączenia $conn. Aplikacja wysyła w nagłówku X-Admin-Token token
// otrzymany przy logowaniu; bez poprawnego tokenu żądanie kończy się tutaj.

$token = $_SERVER['HTTP_X_ADMIN_TOKEN'] ?? '';

$stmt = $conn->prepare("SELECT id FROM admins WHERE token = ?");
$stmt->bind_param("s", $token);
$stmt->execute();

if ($token === '' || $stmt->get_result()->num_rows === 0) {
    echo json_encode(["status" => "error", "message" => "Brak uprawnień"]);
    exit;
}
$stmt->close();
