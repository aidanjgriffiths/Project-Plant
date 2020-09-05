<?php include_once(dirname(__DIR__) . './Root/DatabaseConnector.php');

class PProfile
{
	private $profile_id_pk, $name, $class, $moisture_min, $moisture_max, $temperature_min, $temperature_max, $humidity_min, $humidity_max, $light_min, $light_max;

	public function __construct($name, $class, $moisture_min, $moisture_max, $temperature_min, $temperature_max, $humidity_min, $humidity_max, $light_min, $light_max, $profile_id_pk = null)
	{
		$this->profile_id_pk = $profile_id_pk;
		$this->name = $name;
		$this->class = $class;
		$this->moisture_min = $moisture_min;
		$this->moisture_max = $moisture_max;
		$this->temperature_min = $temperature_min;
		$this->temperature_max = $temperature_max;
		$this->humidity_min = $humidity_min;
		$this->humidity_max = $humidity_max;
		$this->light_min = $light_min;
		$this->light_max = $light_max;
	}

	public function add_pprofile()
	{
		$insert_pprofile_query = "CALL usp_insert_pprofile('{$this->name}', '{$this->class}', '{$this->moisture_min}', '{$this->moisture_max}', '{$this->temperature_min}', '{$this->temperature_max}', '{$this->humidity_min}', '{$this->humidity_max}', '{$this->light_min}', '{$this->light_max}');";
		$result = query($insert_pprofile_query);

		$last_inserted_id = mysqli_fetch_assoc($result)['Last_Inserted_Id'];
		$this->profile_id_pk = $last_inserted_id;

		return $last_inserted_id;
	}

	public static function read_all_pprofile()
	{
		$read_all_pprofile_query = "CALL usp_read_all_pprofile();";
		$result = query($read_all_pprofile_query);
		$pprofiles = array();

		while ($row = mysqli_fetch_assoc($result))
		{
			array_push($pprofiles, new PProfile($row['name'], $row['class'], $row['moisture_min'], $row['moisture_max'], $row['temperature_min'], $row['temperature_max'], $row['humidity_min'], $row['humidity_max'], $row['light_min'], $row['light_max'], $row['profile_id_pk']));
		}
		
		return $pprofiles;
	}

	public static function read_pprofile($profile_id_pk)
	{
		$read_pprofile_query = "CALL usp_read_pprofile('{$profile_id_pk}');";
		$row = mysqli_fetch_assoc(query($read_pprofile_query));
		$pprofile = new PProfile($row['name'], $row['class'], $row['moisture_min'], $row['moisture_max'], $row['temperature_min'], $row['temperature_max'], $row['humidity_min'], $row['humidity_max'], $row['light_min'], $row['light_max'], $row['profile_id_pk']);
		return $pprofile;
	}

	public function update_pprofile()
	{
		$update_pprofile_query = "CALL usp_update_pprofile('{$this->profile_id_pk}', '{$this->name}', '{$this->class}', '{$this->moisture_min}', '{$this->moisture_max}', '{$this->temperature_min}', '{$this->temperature_max}', '{$this->humidity_min}', '{$this->humidity_max}', '{$this->light_min}', '{$this->light_max}');";
		query($update_pprofile_query);
	}

	public function delete_pprofile()
	{
		$delete_pprofile_query = "CALL usp_delete_pprofile('{$this->old_profile_id_pk}');";
		query($delete_pprofile_query);
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