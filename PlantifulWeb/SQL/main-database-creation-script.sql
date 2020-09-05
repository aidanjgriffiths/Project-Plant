-- #### DATABASE CREATION ####

DROP DATABASE IF EXISTS Main;
CREATE DATABASE Main;
USE Main;

-- #### TABLE CREATION ####

CREATE TABLE PWebConnector (
    web_connect_token_pk INT NOT NULL AUTO_INCREMENT,
    web_connect_key VARCHAR(30) NOT NULL,
    PRIMARY KEY (web_connect_token_pk)
);

CREATE TABLE PUser (
    user_id_pk INT NOT NULL AUTO_INCREMENT,
    web_connect_token_fk INT NOT NULL,
    PRIMARY KEY (user_id_pk),
    FOREIGN KEY (web_connect_token_fk)
        REFERENCES PWebConnector (web_connect_token_pk)
);

CREATE TABLE PMeasurement (
    measurement_id_pk INT NOT NULL AUTO_INCREMENT,
    moisture DOUBLE NOT NULL,	
    temperature DOUBLE NOT NULL,
    light DOUBLE NOT NULL,
    humidity DOUBLE NOT NULL,
    PRIMARY KEY (measurement_id_pk)
);

CREATE TABLE PProfile (
    profile_id_pk INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    class VARCHAR(100),
    moisture_min DOUBLE,
    moisture_max DOUBLE,
    temperature_min DOUBLE,
    temperature_max DOUBLE,
    humidity_min DOUBLE,
    humidity_max DOUBLE,
    light_min DOUBLE,
    light_max DOUBLE,
    PRIMARY KEY (profile_id_pk)
);

-- #### STORED PROCEDURES ####

-- USP_INSERT_PWEBCONNECTOR --
DELIMITER //

CREATE PROCEDURE usp_insert_PWebConnector(IN pweb_connect_key VARCHAR(30))
BEGIN
    INSERT 
    INTO PWebConnector (`web_connect_key`)
    VALUES (pweb_connect_key);

    SELECT LAST_INSERT_ID() as Last_Inserted_Id;
END//

DELIMITER ;

-- USP_READ_PWEBCONNECTOR --
DELIMITER //

CREATE PROCEDURE usp_read_PWebConnector(IN pweb_connect_token_pk INT)
BEGIN
    SELECT * 
    FROM PWebConnector 
    WHERE `web_connect_token_pk` = pweb_connect_token_pk;
END//

DELIMITER ;

-- USP_READ_ALL_PWEBCONNECTOR --
DELIMITER //

CREATE PROCEDURE usp_read_all_PWebConnector()
BEGIN
    SELECT * 
    FROM PWebConnector;
END//

DELIMITER ;

-- USP_UPDATE_PWEBCONNECTOR --
DELIMITER //

CREATE PROCEDURE usp_update_PWebConnector(IN pweb_connect_token_pk INT, IN pweb_connect_key VARCHAR(30))
BEGIN
    UPDATE PWebConnector
    SET `web_connect_key` = pweb_connect_key
    WHERE `web_connect_token_pk` = pweb_connect_token_pk;
END//

DELIMITER ;

-- USP_DELETE_PWEBCONNECTOR --
DELIMITER //

CREATE PROCEDURE usp_delete_PWebConnector(IN pweb_connect_token_pk INT)
BEGIN
	DELETE 
	FROM PWebConnector 
	WHERE `web_connect_token_pk` = pweb_connect_token_pk;
END//

DELIMITER ;
-- USP_INSERT_PUSER --
DELIMITER //

CREATE PROCEDURE usp_insert_PUser(IN pweb_connect_token_fk INT)
BEGIN
    INSERT 
    INTO PUser (`web_connect_token_fk`)
    VALUES (pweb_connect_token_fk);

    SELECT LAST_INSERT_ID() as Last_Inserted_Id;
END//

DELIMITER ;

-- USP_READ_PUSER --
DELIMITER //

CREATE PROCEDURE usp_read_PUser(IN puser_id_pk INT)
BEGIN
    SELECT * 
    FROM PUser 
    WHERE `user_id_pk` = puser_id_pk;
END//

DELIMITER ;

-- USP_READ_ALL_PUSER --
DELIMITER //

CREATE PROCEDURE usp_read_all_PUser()
BEGIN
    SELECT * 
    FROM PUser;
END//

DELIMITER ;

-- USP_UPDATE_PUSER --
DELIMITER //

CREATE PROCEDURE usp_update_PUser(IN puser_id_pk INT, IN pweb_connect_token_fk INT)
BEGIN
    UPDATE PUser
    SET `web_connect_token_fk` = pweb_connect_token_fk
    WHERE `user_id_pk` = puser_id_pk;
END//

DELIMITER ;

-- USP_DELETE_PUSER --
DELIMITER //

CREATE PROCEDURE usp_delete_PUser(IN puser_id_pk INT)
BEGIN
	DELETE 
	FROM PUser 
	WHERE `user_id_pk` = puser_id_pk;
END//

DELIMITER ;
-- USP_INSERT_PMEASUREMENT --
DELIMITER //

CREATE PROCEDURE usp_insert_PMeasurement(IN pmoisture DOUBLE(4, 0), IN ptemperature DOUBLE(4, 0), IN plight DOUBLE(4, 0), IN phumidity DOUBLE(4, 0))
BEGIN
    INSERT 
    INTO PMeasurement (`moisture`, `temperature`, `light`, `humidity`)
    VALUES (pmoisture, ptemperature, plight, phumidity);

    SELECT LAST_INSERT_ID() as Last_Inserted_Id;
END//

DELIMITER ;

-- USP_READ_PMEASUREMENT --
DELIMITER //

CREATE PROCEDURE usp_read_PMeasurement(IN pmeasurement_id_pk INT)
BEGIN
    SELECT * 
    FROM PMeasurement 
    WHERE `measurement_id_pk` = pmeasurement_id_pk;
END//

DELIMITER ;

-- USP_READ_ALL_PMEASUREMENT --
DELIMITER //

CREATE PROCEDURE usp_read_all_PMeasurement()
BEGIN
    SELECT * 
    FROM PMeasurement;
END//

DELIMITER ;

-- USP_UPDATE_PMEASUREMENT --
DELIMITER //

CREATE PROCEDURE usp_update_PMeasurement(IN pmeasurement_id_pk INT, IN pmoisture DOUBLE(4, 0), IN ptemperature DOUBLE(4, 0), IN plight DOUBLE(4, 0), IN phumidity DOUBLE(4, 0))
BEGIN
    UPDATE PMeasurement
    SET `moisture` = pmoisture, `temperature` = ptemperature, `light` = plight, `humidity` = phumidity
    WHERE `measurement_id_pk` = pmeasurement_id_pk;
END//

DELIMITER ;

-- USP_DELETE_PMEASUREMENT --
DELIMITER //

CREATE PROCEDURE usp_delete_PMeasurement(IN pmeasurement_id_pk INT)
BEGIN
	DELETE 
	FROM PMeasurement 
	WHERE `measurement_id_pk` = pmeasurement_id_pk;
END//

DELIMITER ;
-- USP_INSERT_PPROFILE --
DELIMITER //

CREATE PROCEDURE usp_insert_PProfile(IN pname VARCHAR(50), IN pclass VARCHAR(100), IN pmoisture_min DOUBLE(7, 2), IN pmoisture_max DOUBLE(7, 2), IN ptemperature_min DOUBLE(7, 2), IN ptemperature_max DOUBLE(7, 2), IN phumidity_min DOUBLE(7, 2), IN phumidity_max DOUBLE(7, 2), IN plight_min DOUBLE(7, 2), IN plight_max DOUBLE(7, 2))
BEGIN
    INSERT 
    INTO PProfile (`name`, `class`, `moisture_min`, `moisture_max`, `temperature_min`, `temperature_max`, `humidity_min`, `humidity_max`, `light_min`, `light_max`)
    VALUES (pname, pclass, pmoisture_min, pmoisture_max, ptemperature_min, ptemperature_max, phumidity_min, phumidity_max, plight_min, plight_max);

    SELECT LAST_INSERT_ID() as Last_Inserted_Id;
END//

DELIMITER ;

-- USP_READ_PPROFILE --
DELIMITER //

CREATE PROCEDURE usp_read_PProfile(IN pprofile_id_pk INT)
BEGIN
    SELECT * 
    FROM PProfile 
    WHERE `profile_id_pk` = pprofile_id_pk;
END//

DELIMITER ;

-- USP_READ_ALL_PPROFILE --
DELIMITER //

CREATE PROCEDURE usp_read_all_PProfile()
BEGIN
    SELECT * 
    FROM PProfile;
END//

DELIMITER ;

-- USP_UPDATE_PPROFILE --
DELIMITER //

CREATE PROCEDURE usp_update_PProfile(IN pprofile_id_pk INT, IN pname VARCHAR(50), IN pclass VARCHAR(100), IN pmoisture_min DOUBLE(7, 2), IN pmoisture_max DOUBLE(7, 2), IN ptemperature_min DOUBLE(7, 2), IN ptemperature_max DOUBLE(7, 2), IN phumidity_min DOUBLE(7, 2), IN phumidity_max DOUBLE(7, 2), IN plight_min DOUBLE(7, 2), IN plight_max DOUBLE(7, 2))
BEGIN
    UPDATE PProfile
    SET `name` = pname, `class` = pclass, `moisture_min` = pmoisture_min, `moisture_max` = pmoisture_max, `temperature_min` = ptemperature_min, `temperature_max` = ptemperature_max, `humidity_min` = phumidity_min, `humidity_max` = phumidity_max, `light_min` = plight_min, `light_max` = plight_max
    WHERE `profile_id_pk` = pprofile_id_pk;
END//

DELIMITER ;

-- USP_DELETE_PPROFILE --
DELIMITER //

CREATE PROCEDURE usp_delete_PProfile(IN pprofile_id_pk INT)
BEGIN
	DELETE 
	FROM PProfile 
	WHERE `profile_id_pk` = pprofile_id_pk;
END//

DELIMITER ;