<?php

include 'AUTH.php';

$conn = new mysqli($HOST, $USER, $PASS);

if ($conn->connect_error)
	die("Unable to Establish a Connection: " . $conn->connect_error);

echo "Connected to Database Successfully!";

?>