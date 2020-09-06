<?php
include './Header.php';
include '../Root/DatabaseConnector.php' ?>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <script src="../Resources/Dependencies/qrcode.js"></script>
        <title>Home</title>
    </head>
    <body>
    <h1>Hello From Body</h1>
    <div id="qr-test">
        <form method="post" action="../Stem/UACStem.php">
            <input id="uid" name="uac_w_uid" type="number" maxlength="6" placeholder="UID">
            <input id="wid" name="uac_w_wid" type="number" maxlength="6" placeholder="WID">
            <input id="submit" type="submit">
        </form>
        <script type="text/javascript">
            document.getElementById("post").onsubmit = function () {
                new QRCode(document.getElementById("qr-test"), document.getElementById("wid").toString());
            }
        </script>
    </div>
    </body>
    </html>
<?php include './Footer.php'; ?>