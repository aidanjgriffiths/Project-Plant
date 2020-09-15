<?php include '../Root/DatabaseConnector.php';

if (isset($_POST['r_q_str']))
{
	$result = query($_POST['r_q_str']);

	if (is_string($result))
	{
		http_response_code(404);
		echo $result;
	}
	else
	{
		http_response_code(200);
		echo "Successful!";
	}
}

?>
