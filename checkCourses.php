   
<?php

$user = "db2";
$mysqli = new mysqli('localhost', 'root', '', 'db2');

if ($mysqli->connect_error) {
    die("Connection failed: " . $mysqli->connect_error);
}

$id = $_POST['id'] ?? '';
$year = 2024;
$semester = 'Spring';

$response = array();
$response["courses"] = array();

$qGetCoursesTaught = "SELECT * FROM take WHERE student_id = ? AND year = ? AND semester = ?";
$stmt = $mysqli->prepare($qGetCoursesTaught);

$stmt->bind_param("iis", $id, $year, $semester);

$stmt->execute();

$result = $stmt->get_result();

if ($result) {
    while ($row = $result->fetch_assoc()) {
        $response["courses"][] = [
            'course_id' => $row['course_id']
        ];
    }
}

$stmt->close();

$mysqli->close();

$response["success"] = true;

echo json_encode($response);

?>