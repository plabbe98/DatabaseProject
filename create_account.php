<?php

// create short variable names
$user = "db2";
$mysqli = new mysqli('localhost', 'root', '', 'db2');

// Check connection
if ($mysqli->connect_error) {
    die("Connection failed: " . $mysqli->connect_error);
}

$student_id = $_POST['student_id'] ?? ''; // Initialize to prevent errors if not set
$name = $_POST['name'] ?? '';
$email = $_POST['email'] ?? '';
$dept_name = $_POST['dept_name'] ?? '';
$password = $_POST['password'] ?? '';
$type = "student";

if($student_id == NULL){
  $response["success"] = true;
}

else{
// INSERT queries
$qInsertStudent = 'INSERT INTO student (student_id, name, email, dept_name, password) VALUES (?, ?, ?, ?, ?)';
$stmtInsertStudent = $mysqli->prepare($qInsertStudent);
$stmtInsertStudent->bind_param("issss", $student_id, $name, $email, $dept_name, $password);

 $qInsertAccount = 'INSERT INTO account (email, password, type) VALUES (?, ?, ?)';
 $stmtInsertAccount = $mysqli->prepare($qInsertAccount);
 $stmtInsertAccount->bind_param("sss", $email, $password, $type);

 $qInsertUndergrad = 'INSERT INTO undergraduate (student_id) VALUES (?)';
 $stmtInsertUndergrad = $mysqli->prepare($qInsertUndergrad);
 $stmtInsertUndergrad->bind_param("i", $student_id);

// Execute INSERT query
if ($stmtInsertStudent->execute()) {
    // SELECT query to get inserted student's information
    $qGetInfo = "SELECT * FROM student WHERE student_id = ?";
    $stmtGetInfo = $mysqli->prepare($qGetInfo);
    $stmtGetInfo->bind_param("i", $student_id);
    $stmtGetInfo->execute();
    
    // Get result of SELECT query
    $result = $stmtGetInfo->get_result();
    
    // Fetch student information
    if ($testrow = $result->fetch_assoc()) {
        $response["name"] = $testrow['name'];
        $response["success"] = true;
    } else {
        $response["success"] = false;
    }
} else {
    $response["success"] = false;
}

$stmtInsertAccount->execute();
$stmtInsertUndergrad->execute();

// Close statements and connection
$stmtInsertStudent->close();
$stmtGetInfo->close();
$stmtInsertUndergrad->close();
$stmtInsertAccount->close();
$mysqli->close();
}

// Send JSON response
header('Content-Type: application/json');
echo json_encode($response);

?>


