   
<?php

$user = "db2";
$mysqli = new mysqli('localhost', 'root', '', 'db2');

if ($mysqli->connect_error) {
    die("Connection failed: " . $mysqli->connect_error);
}

$id = $_POST['id'] ?? '';
$c_id = $_POST['c_id'] ?? '';
$s_id = $_POST['s_id'] ?? '';

$response = array();
$response["current"] = array();
$response["past"] = array();
$response["courses"] = array();
$students = array();

$qGetCoursesTaught = "SELECT * FROM take WHERE course_id = ? AND section_id = ?";
$stmt = $mysqli->prepare($qGetCoursesTaught);

$stmt->bind_param("ss", $c_id, $s_id);

$stmt->execute();

$result = $stmt->get_result();

if ($result) {
    while ($row = $result->fetch_assoc()) {
        $response["courses"][] = [
            'course_id' => $row['course_id'],
            'section_id' => $row['section_id'],
            'semester' => $row['semester'],
            'year' => $row['year'],
	    'name' => $row['student_id'],
	    'grade' => $row['grade']
        ];
    }
}

$stmt->close();

$mysqli->close();

$response["success"] = true;


echo json_encode($response);

?>