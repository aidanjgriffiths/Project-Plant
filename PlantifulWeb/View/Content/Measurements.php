<?php include "../../Model/PMeasurement.php";
include "../../Model/PProfile.php"; ?>
<?php
//$measurements = PMeasurement::read_all_pmeasurement();
//
//$moisture_tot = array();
//$temperature_tot = array();
//$light_tot = array();
//$humidity_tot = array();
//
//$moisture_cou = array();
//$temperature_cou = array();
//$light_cou = array();
//$humidity_cou = array();
//
//$moisture_avg = array();
//$temperature_avg = array();
//$light_avg = array();
//$humidity_avg = array();
//
//foreach ($measurements as $measurement)
//{
//	if (array_key_exists($measurement->profile_id_fk, $moisture_tot))
//	{
//		$moisture_tot[$measurement->profile_id_fk] += $measurement->moisture;
//		$moisture_cou[$measurement->profile_id_fk]++;
//	}
//	else
//	{
//		$moisture_tot[$measurement->profile_id_fk] = $measurement->moisture;
//		$moisture_cou[$measurement->profile_id_fk] = 1;
//	}
//
//	if (array_key_exists($measurement->profile_id_fk, $temperature_tot))
//	{
//		$temperature_tot[$measurement->profile_id_fk] += $measurement->temperature;
//		$temperature_cou[$measurement->profile_id_fk]++;
//	}
//	else
//	{
//		$temperature_tot[$measurement->profile_id_fk] = $measurement->temperature;
//		$temperature_cou[$measurement->profile_id_fk] = 1;
//	}
//
//	if (array_key_exists($measurement->profile_id_fk, $light_tot))
//	{
//		$light_tot[$measurement->profile_id_fk] += $measurement->light;
//		$light_cou[$measurement->profile_id_fk]++;
//	}
//	else
//	{
//		$light_tot[$measurement->profile_id_fk] = $measurement->light;
//		$light_cou[$measurement->profile_id_fk] = 1;
//	}
//
//	if (array_key_exists($measurement->profile_id_fk, $humidity_tot))
//	{
//		$humidity_tot[$measurement->profile_id_fk] += $measurement->humidity;
//		$humidity_cou[$measurement->profile_id_fk]++;
//	}
//	else
//	{
//		$humidity_tot[$measurement->profile_id_fk] = $measurement->humidity;
//		$humidity_cou[$measurement->profile_id_fk] = 1;
//	}
//}
//
//$keys = array_keys($moisture_tot);
//
//foreach ($keys as $key)
//{
//	$moisture_avg[$key] = $moisture_tot[$key] / $moisture_cou[$key];
//    $temperature_avg[$key] = $temperature_tot[$key] / $temperature_cou[$key];
//    $light_avg[$key] = $light_tot[$key] / $light_cou[$key];
//    $humidity_avg[$key] = $humidity_tot[$key] / $humidity_cou[$key];
//}
//
?>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="../../Resources/UI/CSS/Measurements.css">
    <script src="../../Resources/Dependencies/chart.js-2.9.3/Chart.js"></script>
    <title>Measurements</title>
</head>
<body>
<table id="measurement-tbl">
    <thead>
    <tr>
        <td>Measurement</td>
        <td>Graph</td>
    </tr>
    </thead>
    <tbody>
	<?php
	$measurements = PMeasurement::read_all_pmeasurement();
	$measure_count = array('moisture' => 0, 'temperature' => 0, 'light' => 0, 'humidity' => 0);
	$moisture_vals = array();
	$temperature_vals = array();
	$light_vals = array();
	$humidity_vals = array();
	foreach ($measurements as $measurement)
	{
		$measure_count['moisture']++;
		$measure_count['temperature']++;
		$measure_count['light']++;
		$measure_count['humidity']++;

		array_push($moisture_vals, $measurement->moisture);
		array_push($temperature_vals, $measurement->temperature);
		array_push($light_vals, $measurement->light);
		array_push($humidity_vals, $measurement->humidity);
	}
	?>
    <tr>
        <td><?php echo 'Moisture'; ?></td>
        <td class="radial-graph-cls">
            <canvas id="Moisture-graph"></canvas>
        </td>
    </tr>
    <tr>
        <td><?php echo 'Temperature'; ?></td>
        <td class="radial-graph-cls">
            <canvas id="Temperature-graph"></canvas>
        </td>
    </tr>
    <tr>
        <td><?php echo 'Light'; ?></td>
        <td class="radial-graph-cls">
            <canvas id="Light-graph"></canvas>
        </td>
    </tr>
    <tr>
        <td><?php echo 'Humidity'; ?></td>
        <td class="radial-graph-cls">
            <canvas id="Humidity-graph"></canvas>
        </td>
    </tr>
    </tbody>
</table>
<script>
    var dataset = [[<?php echo implode(",", $moisture_vals); ?>],
        [<?php echo implode(",", $temperature_vals); ?>],
        [<?php echo implode(",", $light_vals); ?>],
        [<?php echo implode(",", $humidity_vals); ?>]];
    var graphs = ['Moisture', 'Temperature', 'Light', 'Humidity']
    var counts = [<?php echo $measure_count['moisture']; ?>, <?php echo $measure_count['temperature']; ?>, <?php echo $measure_count['light']; ?>, <?php echo $measure_count['humidity']; ?>];
    for (var i = 0; i < graphs.length; i++) {
        var ctx = document.getElementById(graphs[i] + '-graph');
        ctx.style.backgroundColor = 'rgba(0, 0, 0, 0)';
        var colour = getRandomColor();
        var graph = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: [...Array(counts[i]).keys()],
                datasets: [{
                    label: graphs[i],
                    data: dataset[i],
                    backgroundColor: colour,
                    borderColor: 'rgba(0,0,0,0.5)',
                    borderWidth: 3
                }]
            },
            options: {
                maintainAspectRatio: true,
            }
        });
    }

    function getRandomColor() {
        var letters = '0123456789ABCDEF';
        var color = '#';
        for (var i = 0; i < 6; i++) {
            color += letters[Math.floor(Math.random() * 16)];
        }
        return color;
    }
</script>
</body>
</html>
