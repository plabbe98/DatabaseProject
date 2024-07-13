   
<?php

$user = "db2";
$mysqli = new mysqli('localhost', 'root', '', 'db2');

if ($mysqli->connect_error) {
    die("Connection failed: " . $mysqli->connect_error);
}

$course_id = $_POST['id'] ?? '';
$semester = 'Spring';
$year = '2024';

$response = array();
$response["times"] = array();
$response["sections"] = array();


$qGetCoursesTaken = "SELECT DISTINCT instructor_id FROM section WHERE course_id = ? AND semester = ? AND year = ?";
$stmt = $mysqli->prepare($qGetCoursesTaken);

$stmt->bind_param("ssi", $course_id, $semester, $year);

$stmt->execute();

$result = $stmt->get_result();

if ($result) {
    while ($row = $result->fetch_assoc()) {
        $instructor_id = $row['instructor_id'];

        $qGetOfficeHours = "SELECT DISTINCT time_id FROM office_hours WHERE instructor_id = ?";
        $stmtOfficeHours = $mysqli->prepare($qGetOfficeHours);
        $stmtOfficeHours->bind_param("i", $instructor_id);
        $stmtOfficeHours->execute();
        $resultOfficeHours = $stmtOfficeHours->get_result();
        
        if ($resultOfficeHours) {
            while ($rowOfficeHours = $resultOfficeHours->fetch_assoc()) {
                $time_id = $rowOfficeHours['time_id'];
                
                // Fetch day and time from office_time_slot for each time_id
                $qGetTimeSlot = "SELECT DISTINCT day, start_time FROM office_time_slot WHERE office_id = ?";
                $stmtTimeSlot = $mysqli->prepare($qGetTimeSlot);
                $stmtTimeSlot->bind_param("s", $time_id);
                $stmtTimeSlot->execute();
                $resultTimeSlot = $stmtTimeSlot->get_result();
                
                if ($resultTimeSlot->num_rows > 0) {
                    $rowTimeSlot = $resultTimeSlot->fetch_assoc();
                    $day = $rowTimeSlot['day'];
                    $start_time = $rowTimeSlot['start_time'];
                    
                    // Add day and time to the response
                    $response["times"][] = array(
                        "time_id" => $time_id,
                        "day" => $day,
                        "start_time" => $start_time
                    );
                }
                
                // Close the statement for time slot
                $stmtTimeSlot->close();
            }
        }
        
        // Close the statement for office hours
        $stmtOfficeHours->close();
    }
}

$stmt->close();



$mysqli->close();

$response["success"] = true;

echo json_encode($response);

?>



