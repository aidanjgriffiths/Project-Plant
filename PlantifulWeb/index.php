<!-- ADD FOOTER AND HEADER INSIDE BODY -->
<?php
include 'Root/DatabaseConnector.php';
include 'Root/WebReferences.php';
// set the name of this website
$_POST['W_NAME'] = "Plantiful - Home";
// check if user cookie is set, if not, go to the registration page
//if (!isset($_COOKIE['web_assoc_uid']))
//{
//	header("Location: " . $REG_PAGE);
//	exit;
//}
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
<!--<form id="stem-form" method="post" action="../Stem/UACStem.php">-->
<!--    <input id="uid" name="uac_w_uid" type="number" maxlength="6" placeholder="UID">-->
<!--    <input id="wid" name="uac_w_wid" type="number" maxlength="6" placeholder="WID">-->
<!--    <input id="submit" type="submit">-->
<!--</form>-->
<!--<div id="qr-test"></div>-->
<!--<script type="text/javascript">-->
<!--    new QRCode(document.getElementById("qr-test"), "123456789");-->
<!--    document.getElementById("post").onsubmit = function () {-->
<!---->
<!--    }-->
<!--</script>-->
<iframe id="root-display" src="https://fr.wikipedia.org/wiki/Main_Page">
</iframe>
<?php include 'View/Footer.php'; ?>
</body>
</html>
