<?php

$user = "db2";
$mysqli = new mysqli('localhost', 'root', '', 'db2');

if ($mysqli->connect_error) {
    die("Connection failed: " . $mysqli->connect_error);
}

$id = $_POST['id'] ?? '';
$semester = 'Spring';
$year = 2024;

$response = array();
$response["times"] = array();
$response["busy"] = array();
$response["checked"] = array();

// Fetch busy time slots for the instructor
$qUnavailability = "SELECT time_slot_id FROM section WHERE instructor_id = ? AND semester = ? AND year = ?";
$stmt2 = $mysqli->prepare($qUnavailability);
$stmt2->bind_param("isi", $id, $semester, $year);

$stmt2->execute();
$stmt2->bind_result($time_slot_id);

while ($stmt2->fetch()) {
    $time_slot_ids[] = $time_slot_id;
}

$stmt2->close();

// Fetch busy time slots details
if (!empty($time_slot_ids)) {
    $qBusy = "SELECT day, start_time, end_time FROM time_slot WHERE time_slot_id = ?";
    $stmt3 = $mysqli->prepare($qBusy);

    foreach ($time_slot_ids as $time_slot_id) {
        $stmt3->bind_param("i", $time_slot_id);
        $stmt3->execute();
        $stmt3->bind_result($day, $start_time, $end_time);
        while ($stmt3->fetch()) {
            $response["busy"][] = [
                'day' => $day,
                'start_time' => $start_time,
                'end_time' => $end_time
            ];
        }
    }
    $stmt3->close();

    // Sort busy time slots by day of the week
    $dayOrder = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    usort($response["busy"], function($a, $b) use ($dayOrder) {
        return array_search($a['day'], $dayOrder) - array_search($b['day'], $dayOrder);
    });
}

// Fetch office time slots
$qGetCoursesTaught = "SELECT * FROM office_time_slot";
$stmt = $mysqli->prepare($qGetCoursesTaught);
$stmt->execute();
$result = $stmt->get_result();

if ($result) {
    while ($row = $result->fetch_assoc()) {
        $response["times"][] = [
            'office_id' => $row['office_id'],
            'day' => $row['day'],
            'start_time' => $row['start_time'],
            'end_time' => $row['end_time']
        ];
    }
}

$stmt->close();

// Sort office time slots by day of the week
usort($response["times"], function($a, $b) use ($dayOrder) {
    return array_search($a['day'], $dayOrder) - array_search($b['day'], $dayOrder);
});

$qGetChecked = "SELECT time_id FROM office_hours WHERE instructor_id = ?";
$stmtc = $mysqli->prepare($qGetChecked);
$stmtc->bind_param("i", $id);
$stmtc->execute();
$resultc = $stmtc->get_result(); // Corrected variable name

if ($resultc) {
    while ($row = $resultc->fetch_assoc()) {
        $response["checked"][] = [
            'time_id' => $row['time_id']
        ];
    }
}
$stmtc->close();

$mysqli->close();

$response["success"] = true;

echo json_encode($response);

?>
