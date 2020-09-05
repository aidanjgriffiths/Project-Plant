<?php

include 'AUTH.php';

function connect()
{
	global $HOST, $USER, $PASS;

	$conn = new mysqli($HOST, $USER, $PASS);
	if ($conn->connect_error)
		die("Unable to Establish a Connection: " . $conn->connect_error);

	return $conn;
}

function query($query_string)
{
	global $HOST, $USER, $PASS;

	$conn = connect();
	$result = $conn->query($query_string);
	$conn->close();

	return $result;
}

?>