# java-project-team3
CICCC's project that demonstrates user authentification and authorization using jwt. 

### Member
- Musashi
- Koro
- Chihiro
- Chizuru

### DB Schema
![db_schema_for_java_project](https://user-images.githubusercontent.com/14939662/89093735-c6e6c880-d371-11ea-84c6-fe9a42f0ccc3.jpg)

### how to setup
- replace the placeholders with your credentials
```
package db;

public class DBConstants {
	public static final String USERNAME = "YOUR_USERNAME";
	public static final String PASSWORD = "YOUR_PASSWORD";
	public static final String CONN_STRING = "jdbc:mysql://localhost:3306/java_project?serverTimezone=UTC";
}

```
- create db
After create db, run the command below in order to create database.
```
SET GLOBAL event_scheduler = ON;

DROP DATABASE IF EXISTS java_project;
CREATE DATABASE java_project;

USE java_project;

CREATE TABLE User (
	id	INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    isAdmin BOOLEAN DEFAULT FALSE
);

CREATE TABLE Token (
	id	VARCHAR(255) PRIMARY KEY,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE EVENT IF NOT EXISTS `clean_token_expired`
    ON SCHEDULE
      EVERY 1 MINUTE
        DO
			DELETE FROM Token WHERE TIMESTAMPDIFF(MINUTE, created_at, NOW()) > 1;
```
