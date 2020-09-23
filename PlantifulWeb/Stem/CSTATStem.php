<?php include '../Root/DatabaseConnector.php';

if (isset($_GET['w_id']))
{
	$query = /** @lang text */
		"SELECT pu.user_id_pk FROM PWebConnector pw, PUser pu WHERE pw.web_connect_key = '{$_GET['w_id']}' AND pw.web_connect_pk = pu.web_connect_key_pk_fk;";

	$result = query($query);

	if (is_string($result))
	{
		http_response_code(404);
		echo $result;
	}
	else
	{
		http_response_code(200);

		$r = $result->fetch_row();
		echo $r[0];
	}
}

?>
