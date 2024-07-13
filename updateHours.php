<?php

$mysqli = new mysqli('localhost', 'root', '', 'db2');
if ($mysqli->connect_error) {
    die("Connection failed: " . $mysqli->connect_error);
}

$id = $_POST['id'] ?? '';
$jsonData = $_POST['json_data'] ?? '';
$unjsonData = $_POST['unjson_data'] ?? '';
$dataArray = json_decode($jsonData, true);
$undataArray = json_decode($unjsonData, true);

$response = array();
$response["success"] = false;
$response["message"] = "No data processed";


if (!empty($undataArray)) {
    foreach ($undataArray as $undata) {
        $day2 = $undata['day'];
        $time2 = $undata['time'];

        $qGetTimes2 = "SELECT office_id FROM office_time_slot WHERE day = ? AND start_time = ?";
        $stmt2 = $mysqli->prepare($qGetTimes2);
        $stmt2->bind_param("ss", $day2, $time2);
        $stmt2->execute();
        $result2 = $stmt2->get_result();

        if ($result2->num_rows > 0) {
                $response["success"] = true;
                while ($row2 = $result2->fetch_assoc()) {
                    $office_id2 = $row2['office_id'];

        		$qDeleteOfficeHours = "DELETE FROM office_hours WHERE time_id = ? AND instructor_id = ?";
       	 		$stmtDelete = $mysqli->prepare($qDeleteOfficeHours);
        		$stmtDelete->bind_param("si", $office_id2, $id);
        		$successDelete = $stmtDelete->execute();
			$stmtDelete->close();

        	}

        }
    }
}

if (!empty($dataArray)) {
    // Query the office_hours table to retrieve existing pairs
    $qGetExistingPairs = "SELECT time_id FROM office_hours WHERE instructor_id = ?";
    $stmtGetExistingPairs = $mysqli->prepare($qGetExistingPairs);
    $stmtGetExistingPairs->bind_param("i", $id);
    $stmtGetExistingPairs->execute();
    $resultExistingPairs = $stmtGetExistingPairs->get_result();

    // Store existing time_ids in an array
    $existingTimeIds = array();
    while ($rowExistingPair = $resultExistingPairs->fetch_assoc()) {
        $existingTimeIds[] = $rowExistingPair['time_id'];
    }

    // Close the statement
    $stmtGetExistingPairs->close();

    // Loop through each item in the JSON array
    foreach ($dataArray as $item) {
        $day = $item['day'];
        $time = $item['time'];

        // Prepare and execute SQL query to get office_id
        $qGetTimes = "SELECT office_id FROM office_time_slot WHERE day = ? AND start_time = ?";
        $stmt = $mysqli->prepare($qGetTimes);
        $stmt->bind_param("ss", $day, $time);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows > 0) {
            // Fetch the office_id
            $row = $result->fetch_assoc();
            $office_id = $row['office_id'];

            // Check if the pair (time_id, instructor_id) already exists
            if (!in_array($office_id, $existingTimeIds)) {
                // If not, insert the pair into the office_hours table
                $qInsertOfficeHours = "INSERT INTO office_hours (time_id, instructor_id) VALUES (?, ?)";
                $stmtInsert = $mysqli->prepare($qInsertOfficeHours);
                $stmtInsert->bind_param("si", $office_id, $id);
                $success = $stmtInsert->execute();
                $stmtInsert->close();

                // Update response based on success
                if ($success) {
                    $response["success"] = true;
                    $response["message"] = "Office hours updated successfully";
                } else {
                    $response["message"] = "Failed to update office hours";
                }
            } else {
                $response["message"] = "Pair (time_id: $office_id, instructor_id: $id) already exists";
            }
        } else {
            $response["message"] = "No matching time slot found for day: $day and time: $time";
        }
    }
}

	$stmt->close();
	$mysqli->close();

	echo json_encode($response);

?>

