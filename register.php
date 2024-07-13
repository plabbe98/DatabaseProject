<?php

$user = "db2";
$mysqli = new mysqli('localhost', 'root', '', 'db2');

if ($mysqli->connect_error) {
    die("Connection failed: " . $mysqli->connect_error);
}

$student_id = $_POST['id'] ?? '';
$course_id = $_POST['c_id'] ?? '';
$section_id_reg = $_POST['s_id'] ?? '';
$semester = 'Fall';
$year = 2024;

  $query = 'SELECT numEnrolled FROM section WHERE section_id = ? AND semester = ? AND year = ?';
  $stmt = $mysqli->prepare($query);
  $stmt->bind_param("ssi", $section_id_reg, $semester, $year);
  $stmt->execute();
  $stmt->bind_result($numEnrolled); // Bind result variables
  $stmt->fetch(); // Fetch the result
  $stmt->close();

  if($numEnrolled < 15){

    $addStudent = $numEnrolled + 1;

    $query = 'UPDATE section SET numEnrolled = ? WHERE section_id = ? AND course_id = ? AND year = ? AND semester = ?';
    $stmt = $mysqli->prepare($query);
    $stmt->bind_param("issss", $addStudent, $section_id_reg, $course_id, $year, $semester);
    $stmt->execute();
    $stmt->close();

    $query = 'SELECT prereq_id FROM prereq WHERE course_id = ?';
    $stmt = $mysqli->prepare($query);
    $stmt->bind_param("s", $course_id);
    $stmt->execute();
    $stmt->bind_result($prereq_id); // Bind result variables
    $stmt->fetch(); // Fetch the result
    $stmt->close();

    if($prereq_id){
      $query = 'SELECT course_id, semester, year, section_id FROM take WHERE course_id = ? AND student_id = ?';
      $stmt = $mysqli->prepare($query);
      $stmt->bind_param("si", $prereq_id, $student_id);
      $stmt->execute();
      $stmt->bind_result($result_course_id, $semester, $year, $section_id); // Bind result variables
      $stmt->fetch(); // Fetch the result
      $stmt->close();

      if($result_course_id){
        $semester = 'Fall';
        $year = 2024;

        $query = 'INSERT INTO take (student_id, course_id, section_id, semester, year) VALUES (?, ?, ?, ?, ?)';
        $stmt = $mysqli->prepare($query);
        $stmt->bind_param("isssi", $student_id, $course_id, $section_id_reg, $semester, $year);
        $stmt->execute();
        $stmt->close();
      }
      else{
        echo '<script>alert("Student does not meet prerequisite requirement!")</script>';
        header("Refresh: 0; URL=studentmenu.php");
      }
  }
  else{
     $semester = 'Fall';
     $year = 2024;
     $query = 'INSERT INTO take (student_id, course_id, section_id, semester, year) VALUES (?, ?, ?, ?, ?)';
     $stmt = $mysqli->prepare($query);
     $stmt->bind_param("isssi", $student_id, $course_id, $section_id_reg, $semester, $year);
     $stmt->execute();
     $stmt->close();
  }
 }
 else{
    echo '<script>alert("This section is at capacity!")</script>';
    header("Refresh: 5; URL=studentmenu.php");
   }

$response["success"] = true;

echo json_encode($response);

?>



