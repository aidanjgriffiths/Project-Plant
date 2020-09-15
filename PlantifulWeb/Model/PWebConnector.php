<?php include_once(dirname(__DIR__) . './Root/DatabaseConnector.php');

class PWebConnector
{
	private $web_connect_pk, $web_connect_key;

	public function __construct($web_connect_key, $web_connect_pk = null)
	{
		$this->web_connect_pk = $web_connect_pk;
		$this->web_connect_key = $web_connect_key;
	}

	public function add_pwebconnector()
	{
		$insert_pwebconnector_query = "CALL usp_insert_pwebconnector('{$this->web_connect_key}');";
		$result = query($insert_pwebconnector_query);

		$last_inserted_id = mysqli_fetch_assoc($result)['Last_Inserted_Id'];
		$this->web_connect_pk = $last_inserted_id;

		return $last_inserted_id;
	}

	public static function read_all_pwebconnector()
	{
		$read_all_pwebconnector_query = "CALL usp_read_all_pwebconnector();";
		$result = query($read_all_pwebconnector_query);
		$pwebconnectors = array();

		while ($row = mysqli_fetch_assoc($result))
		{
			array_push($pwebconnectors, new PWebConnector($row['web_connect_key'], $row['web_connect_pk']));
		}
		
		return $pwebconnectors;
	}

	public static function read_pwebconnector($web_connect_pk)
	{
		$read_pwebconnector_query = "CALL usp_read_pwebconnector('{$web_connect_pk}');";
		$row = mysqli_fetch_assoc(query($read_pwebconnector_query));
		$pwebconnector = new PWebConnector($row['web_connect_key'], $row['web_connect_pk']);
		return $pwebconnector;
	}

	public function update_pwebconnector()
	{
		$update_pwebconnector_query = "CALL usp_update_pwebconnector('{$this->web_connect_pk}', '{$this->web_connect_key}');";
		query($update_pwebconnector_query);
	}

	public function delete_pwebconnector()
	{
		$delete_pwebconnector_query = "CALL usp_delete_pwebconnector('{$this->old_web_connect_pk}');";
		query($delete_pwebconnector_query);
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