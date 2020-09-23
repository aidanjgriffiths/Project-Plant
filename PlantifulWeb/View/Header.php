<div id="hd-div" class="foot-head-cls">
    <div id="heading-div">
        <h1 id="phome-header"><?php echo $_POST['W_NAME'] ?></h1>
    </div>
    <div id="user-det-div">
        <p>User ID: <?php
			if (isset($_COOKIE['web_assoc_uid']))
				echo $_COOKIE['web_assoc_uid'];
			else
				echo 'No User Active'; ?></p>
    </div>
</div>