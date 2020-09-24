<script>
    function changesrc(direction) {
        switch (direction) {
            case 0:
                document.getElementById("root-display").src = "View/Content/Overview.php";
                break;
            case 1:
                document.getElementById("root-display").src = "View/Content/Calendar.php";
                break;
            case 2:
                document.getElementById("root-display").src = "View/Content/Measurements.php";
                break;
        }
    }
</script>
<div id="ft-div" class="foot-head-cls">
    <input id="overview-img" type="image" src="Resources/UI/Overview.png"
           onclick="changesrc(0)" alt="Overview">
    <input id="overview-img" type="image" src="Resources/UI/Calendar.png"
           onclick="changesrc(2)" alt="Calendar">
</div>
