DROP DATABASE IF EXISTS Main;
CREATE DATABASE Main;
USE Main;

CREATE TABLE PWebConnector (
    web_connect_token_pk INT NOT NULL AUTO_INCREMENT,
    web_connect_key NVARCHAR(30) NOT NULL,
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
    profile_name NVARCHAR(50) NOT NULL,
    PRIMARY KEY (profile_id_pk)
);