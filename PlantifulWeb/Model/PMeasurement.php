<?php include_once(dirname(__DIR__) . './Root/DatabaseConnector.php');

class PMeasurement
{
	private $measurement_id_pk, $moisture, $temperature, $light, $humidity, $profile_id_fk, $user_id_fk;

	public function __construct($moisture, $temperature, $light, $humidity, $profile_id_fk, $user_id_fk, $measurement_id_pk = null)
	{
		$this->measurement_id_pk = $measurement_id_pk;
		$this->moisture = $moisture;
		$this->temperature = $temperature;
		$this->light = $light;
		$this->humidity = $humidity;
		$this->profile_id_fk = $profile_id_fk;
		$this->user_id_fk = $user_id_fk;
	}

	public function add_pmeasurement()
	{
		$insert_pmeasurement_query = "CALL usp_insert_pmeasurement('{$this->moisture}', '{$this->temperature}', '{$this->light}', '{$this->humidity}', '{$this->profile_id_fk}', '{$this->user_id_fk}');";
		$result = query($insert_pmeasurement_query);

		$last_inserted_id = mysqli_fetch_assoc($result)['Last_Inserted_Id'];
		$this->measurement_id_pk = $last_inserted_id;

		return $last_inserted_id;
	}

	public static function read_all_pmeasurement()
	{
		$read_all_pmeasurement_query = "CALL usp_read_all_pmeasurement();";
		$result = query($read_all_pmeasurement_query);
		$pmeasurements = array();

		while ($row = mysqli_fetch_assoc($result))
		{
			array_push($pmeasurements, new PMeasurement($row['moisture'], $row['temperature'], $row['light'], $row['humidity'], $row['profile_id_fk'], $row['user_id_fk'], $row['measurement_id_pk']));
		}
		
		return $pmeasurements;
	}

	public static function read_pmeasurement($measurement_id_pk)
	{
		$read_pmeasurement_query = "CALL usp_read_pmeasurement('{$measurement_id_pk}');";
		$row = mysqli_fetch_assoc(query($read_pmeasurement_query));
		$pmeasurement = new PMeasurement($row['moisture'], $row['temperature'], $row['light'], $row['humidity'], $row['profile_id_fk'], $row['user_id_fk'], $row['measurement_id_pk']);
		return $pmeasurement;
	}

	public function update_pmeasurement()
	{
		$update_pmeasurement_query = "CALL usp_update_pmeasurement('{$this->measurement_id_pk}', '{$this->moisture}', '{$this->temperature}', '{$this->light}', '{$this->humidity}', '{$this->profile_id_fk}', '{$this->user_id_fk}');";
		query($update_pmeasurement_query);
	}

	public function delete_pmeasurement()
	{
		$delete_pmeasurement_query = "CALL usp_delete_pmeasurement('{$this->old_measurement_id_pk}');";
		query($delete_pmeasurement_query);
	}

	public function __get($property)
	{
		if (property_exists($this, $property))
		{
			return $this->$property;
		}
		else
		{
			return null;
		}
	}
	
	public function __set($property, $value)
	{
		if (property_exists($this, $property))
		{
			$this->$property = $value;
		}
		
		return $this;
	}
}

?>