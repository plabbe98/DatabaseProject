   
<?php

$user = "db2";
$mysqli = new mysqli('localhost', 'root', '', 'db2');

if ($mysqli->connect_error) {
    die("Connection failed: " . $mysqli->connect_error);
}

$student_id = $_POST['id'] ?? '';

$response = array();
$response["courses"] = array();

$qGetCoursesTaken = "SELECT course_id, section_id, semester, year, grade FROM take WHERE student_id = ?";
$stmt = $mysqli->prepare($qGetCoursesTaken);

$stmt->bind_param("i", $student_id);

$stmt->execute();

$result = $stmt->get_result();

if ($result) {
    while ($row = $result->fetch_assoc()) {
        $response["courses"][] = [
            'course_id' => $row['course_id'],
            'section_id' => $row['section_id'],
            'semester' => $row['semester'],
            'year' => $row['year'],
            'grade' => $row['grade']
        ];
    }
    
}

$stmt->close();
$mysqli->close();

$response["success"] = true;

echo json_encode($response);

?>



