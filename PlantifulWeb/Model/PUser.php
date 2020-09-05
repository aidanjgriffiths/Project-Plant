<?php include_once(dirname(__DIR__) . './Root/DatabaseConnector.php');

class PUser
{
	private $user_id_pk, $web_connect_token_fk;

	public function __construct($web_connect_token_fk, $user_id_pk = null)
	{
		$this->user_id_pk = $user_id_pk;
		$this->web_connect_token_fk = $web_connect_token_fk;
	}

	public function add_puser()
	{
		$insert_puser_query = "CALL usp_insert_puser('{$this->web_connect_token_fk}');";
		$result = query($insert_puser_query);

		$last_inserted_id = mysqli_fetch_assoc($result)['Last_Inserted_Id'];
		$this->user_id_pk = $last_inserted_id;

		return $last_inserted_id;
	}

	public static function read_all_puser()
	{
		$read_all_puser_query = "CALL usp_read_all_puser();";
		$result = query($read_all_puser_query);
		$pusers = array();

		while ($row = mysqli_fetch_assoc($result))
		{
			array_push($pusers, new PUser($row['web_connect_token_fk'], $row['user_id_pk']));
		}
		
		return $pusers;
	}

	public static function read_puser($user_id_pk)
	{
		$read_puser_query = "CALL usp_read_puser('{$user_id_pk}');";
		$row = mysqli_fetch_assoc(query($read_puser_query));
		$puser = new PUser($row['web_connect_token_fk'], $row['user_id_pk']);
		return $puser;
	}

	public function update_puser()
	{
		$update_puser_query = "CALL usp_update_puser('{$this->user_id_pk}', '{$this->web_connect_token_fk}');";
		query($update_puser_query);
	}

	public function delete_puser()
	{
		$delete_puser_query = "CALL usp_delete_puser('{$this->old_user_id_pk}');";
		query($delete_puser_query);
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