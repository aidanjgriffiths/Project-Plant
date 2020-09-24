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
    <link rel="stylesheet" href="../../Resources/UI/CSS/Overview.css">
    <script src="../../Resources/Dependencies/chart.js-2.9.3/Chart.js"></script>
    <title>Overview Statistics</title>
</head>
<body>
<table id="overview-tbl">
    <thead>
    <tr>
        <td>Moisture</td>
        <td>Temperature</td>
        <td>Light</td>
        <td>Humidity</td>
    </tr>
    </thead>
    <tbody>
	<?php
	$measurements = PMeasurement::read_all_pmeasurement();
	$measure_count = array('moisture' => 0, 'temperature' => 0, 'light' => 0, 'humidity' => 0);
	$measure_avg = array('moisture' => 0, 'temperature' => 0, 'light' => 0, 'humidity' => 0);
	foreach ($measurements as $measurement)
	{
		$measure_count['moisture']++;
		$measure_avg['moisture'] += $measurement->moisture;

		$measure_count['temperature']++;
		$measure_avg['temperature'] += $measurement->temperature;

		$measure_count['light']++;
		$measure_avg['light'] += $measurement->light;

		$measure_count['humidity']++;
		$measure_avg['humidity'] += $measurement->humidity;
	}
	?>
    <tr>
        <td><?php echo round($measure_avg['moisture'] / $measure_count['moisture'], 1); ?></td>
        <td><?php echo round($measure_avg['temperature'] / $measure_count['temperature'], 1); ?></td>
        <td><?php echo round($measure_avg['light'] / $measure_count['light'], 1); ?></td>
        <td><?php echo round($measure_avg['humidity'] / $measure_count['humidity'], 1); ?></td>
    </tr>
    <tr>
        <td colspan="4">
            <div>
                <canvas id="avg-graph"></canvas>
            </div>
        </td>
    </tr>
    </tbody>
</table>
<script>
    var ctx = document.getElementById('avg-graph');
    var avg_graph = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['Moisture', 'Temperature', 'Light', 'Humidity'],
            datasets: [{
                label: 'Average of Measurements',
                data: [<?php echo round($measure_avg['moisture'] / $measure_count['moisture'], 1) . ', ' .
					round($measure_avg['temperature'] / $measure_count['temperature'], 1) . ', ' .
					round($measure_avg['light'] / $measure_count['light'], 1) . ', ' .
					round($measure_avg['humidity'] / $measure_count['humidity'], 1);
					?>],
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(54, 162, 235, 0.2)',
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(75, 192, 192, 0.2)',
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)',
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        }
    });
</script>
</body>
</html>
