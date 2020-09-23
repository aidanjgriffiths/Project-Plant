<?php

if (!isset($_COOKIE['qr_seed']))
{
	setcookie("qr_seed", rand());
	header("Refresh:0");
}

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
    <p id="connect-status">Waiting for Connection...<img id="loading" src="../Resources/UI/loading.png" alt=""></p>
    <script type="text/javascript">
        async function await_connection() {
            var logged_in = false;

            while (!logged_in) {
                var xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function (oEvent) {
                    if (this.readyState === 4 && this.status === 200) {
                        if (this.responseText[0] >= 0) {
                            document.getElementById("connect-status").textContent = "Connected to User: " + this.responseText;
                            sleep(2000);
                            document.location.href = "../index.php?uid=" + this.responseText;
                            logged_in = true;
                        } else
                            console.log(this.responseText);
                    }
                };
                xhttp.open("GET", "../Stem/CSTATStem.php?w_id=" + <?php echo $_COOKIE['qr_seed']; ?>, true);
                xhttp.send();
                await sleep(2000);
            }
        }

        function sleep(time) {
            return new Promise((resolve) => setTimeout(resolve, time));
        }

        await_connection();
    </script>
</div>
<div id="qr-cont">
    <script type="text/javascript">
        new QRCode(document.getElementById("qr-cont"), "<?php echo $_COOKIE['qr_seed']; ?>");
    </script>
</div>
<div id="contributor-det-div">
    <img id="dev-team-img" src="../Resources/UI/Dev-Team.png" alt="Development Team">
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
