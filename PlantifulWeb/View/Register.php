<?php

?>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="../Resources/UI/CSS/Register.css">
    <script src="../Resources/Dependencies/qrcode.js"></script>
    <title>Register</title>
</head>
<body>
<div id="plantiful-div">
    <h1 id="plantiful-hd">Plantiful Web<img id="plantiful-leaf" src="../Resources/UI/mint-leaf-icon.png" alt=""></h1>
    <p id="plantiful-direct">Please scan the following QR Code using the Web Sync function on the Mobile Application</p>
</div>
<div id="qr-cont">
    <script type="text/javascript">
        new QRCode(document.getElementById("qr-cont"), "<?php echo rand(); ?>");
    </script>
</div>
<div id="contributor-det-div">
    <img id="dev-team-img" src="../Resources/dev-team.png" alt="Development Team">
    <div id="contributor-det-inner-div">
        <p id="plantiful-dev-team-p">
            Plantiful Team<br>
        </p>
        <p id="plantiful-dev-team-names-p">
            Ash Phillips,
            <br>Mario Manitta,
            <br>Aidan Griffiths,
            <br>Miles Danswan
        </p>
    </div>
</div>
</body>
</html>
