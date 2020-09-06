<?php include_once(dirname(__DIR__) . '/Root/DatabaseConnector.php');

class PUser
{
	private $user_id_pk, $web_connect_token_pk_fk;

	public function __construct($user_id_pk = null, $web_connect_token_pk_fk = null)
	{
		$this->user_id_pk = $user_id_pk;
		$this->web_connect_token_pk_fk = $web_connect_token_pk_fk;
	}

	public static function read_all_puser()
	{
		$read_all_puser_query = "CALL usp_read_all_puser();";
		$result = query($read_all_puser_query);
		$pusers = array();

		while ($row = mysqli_fetch_assoc($result))
		{
			array_push($pusers, new PUser($row['user_id_pk'], $row['web_connect_token_pk_fk']));
		}

		return $pusers;
	}

	public static function read_puser($user_id_pk, $web_connect_token_pk_fk)
	{
		$read_puser_query = "CALL usp_read_puser('{$user_id_pk}', '{$web_connect_token_pk_fk}');";
		$row = mysqli_fetch_assoc(query($read_puser_query));
		$puser = new PUser($row['user_id_pk'], $row['web_connect_token_pk_fk']);
		return $puser;
	}

	public function delete_puser()
	{
		$delete_puser_query = "CALL usp_delete_puser('{$this->old_user_id_pk}', '{$this->old_web_connect_token_pk_fk}');";
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