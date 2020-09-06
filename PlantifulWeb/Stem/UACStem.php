<?php

include '../Root/DatabaseConnector.php';
include '../Model/PUser.php';
include '../Model/PWebConnector.php';

if (isset($_POST['uac_w_uid']) and isset($_POST['uac_w_wid']))
{
	$UID = $_POST['uac_w_uid'];
	$WID = $_POST['uac_w_wid'];

	http_response_code('404'); // Default: Unsuccessful

	$wcon = new PWebConnector($WID);
	$nid = $wcon->add_pwebconnector();

	$user = new PUser($UID, $nid);
	$user->add_puser();

	http_response_code('200'); // Success
}

?>