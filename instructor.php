   
<?php

$user = "db2";
$mysqli = new mysqli('localhost', 'root', '', 'db2');

if ($mysqli->connect_error) {
    die("Connection failed: " . $mysqli->connect_error);
}

$id = $_POST['id'] ?? '';

$response = array();
$response["courses"] = array();

$qGetCoursesTaught = "SELECT course_id, section_id FROM section WHERE instructor_id = ?";
$stmt = $mysqli->prepare($qGetCoursesTaught);

$stmt->bind_param("i", $id);

$stmt->execute();

$result = $stmt->get_result();

if ($result) {
    while ($row = $result->fetch_assoc()) {
        $response["courses"][] = [
            'course_id' => $row['course_id'],
            'section_id' => $row['section_id']
        ];
    }
    
}

$stmt->close();
$mysqli->close();

$response["success"] = true;


echo json_encode($response);

?>
