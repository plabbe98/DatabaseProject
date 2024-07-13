   
<?php

$user = "db2";
$mysqli = new mysqli('localhost', 'root', '', 'db2');

if ($mysqli->connect_error) {
    die("Connection failed: " . $mysqli->connect_error);
}

$semester = 'Fall';
$year = '2024';

$response = array();
$response["courses"] = array();

$qGetCoursesTaken = "SELECT course_id, section_id FROM section WHERE semester = ? AND year = ?";
$stmt = $mysqli->prepare($qGetCoursesTaken);

$stmt->bind_param("si", $semester, $year);

$stmt->execute();

$result = $stmt->get_result();

if ($result) {
    while ($row = $result->fetch_assoc()) {
        $response["courses"][] = [
            'course_id' => $row['course_id'],
            'section_id' => $row['section_id'],
        ];
    }
    
}

$stmt->close();
$mysqli->close();

$response["success"] = true;

echo json_encode($response);

?>