<!-- ADD FOOTER AND HEADER INSIDE BODY -->
<?php
include 'Root/DatabaseConnector.php';
include 'Root/WebReferences.php';
// set the name of this website
$_POST['W_NAME'] = "Plantiful - Home";
// check if user cookie is set, if not, go to the registration page
if (isset($_GET['uid']) && !isset($_COOKIE['web_assoc_uid']))
{
    setcookie('web_assoc_uid', $_GET['uid']);
    header('Refresh:0');
}
else if (!isset($_COOKIE['web_assoc_uid']))
{
	header("Location: " . $REG_PAGE);
	exit;
}
?>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="Resources/UI/CSS/Base.css">
    <link rel="stylesheet" href="Resources/UI/CSS/Index.css">
    <link rel="stylesheet" href="Resources/UI/CSS/Header.css">
    <link rel="stylesheet" href="Resources/UI/CSS/Footer.css">
    <title></title>
</head>
<body>
<?php include 'View/Header.php'; ?>
<iframe id="root-display" src="./View/Content/Overview.php"></iframe>
<?php include 'View/Footer.php'; ?>
</body>
</html>
