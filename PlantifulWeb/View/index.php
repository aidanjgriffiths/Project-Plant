<?php include './Header.php'; ?>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <script src="../Dependencies/qrcode.js"></script>
        <title>Home</title>
    </head>
    <body>
    <h1>Hello From Body</h1>
    <div id="qr-test">
        <script type="text/javascript">
            new QRCode(document.getElementById("qr-test"), "12345678910121242342353463645");
        </script>
    </div>
    </body>
    </html>
<?php include './Footer.php'; ?>