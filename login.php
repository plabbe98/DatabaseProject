<?php

$user = "db2";
$mysqli = new mysqli('localhost', 'root', '', 'db2');

if ($mysqli->connect_error) {
    die("Connection failed: " . $mysqli->connect_error);
}

$email = $_POST['email'] ?? '';
$password = $_POST['password'] ?? '';

$qGetInfo = "SELECT type FROM account WHERE email = '$email' AND password = '$password'";
$result = $mysqli->query($qGetInfo);
$testrow = mysqli_fetch_array($result);

if(empty($testrow)){
    $response["success"] = "false";
    echo json_encode($response);
}



else{
  $response["type"] = $testrow['type'];
  if($testrow['type'] == 'student'){
    $qGetStudent = "SELECT name, student_id FROM student WHERE email = '$email'";
    $result = $mysqli->query($qGetStudent);
    $student = mysqli_fetch_array($result);
    $response["name"] = $student['name'];
    $response["id"] = $student['student_id'];
    $response["email"] = $email;
    $response["password"] = $password;

  }
  else if($testrow['type'] == 'instructor'){
    $qGetInstructor = "SELECT instructor_name, instructor_id FROM instructor WHERE email = '$email'";
    $result = $mysqli->query($qGetInstructor);
    $instructor = mysqli_fetch_array($result);
    $response["name"] = $instructor['instructor_name'];
    $response["id"] = $instructor['instructor_id'];
    $response["email"] = $email;
    $response["password"] = $password;
  }
}

    $response["success"] = "true";
    echo json_encode($response);

?>