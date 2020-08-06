# java-project-team3
## Musashi, Koro, Chihiro, Chizuru

## DB Schema
![db_schema_for_java_project](https://user-images.githubusercontent.com/14939662/89093735-c6e6c880-d371-11ea-84c6-fe9a42f0ccc3.jpg)

### how to setup
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
	id	VARCHAR(500) PRIMARY KEY,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE EVENT IF NOT EXISTS `clean_token_expired`
    ON SCHEDULE
      EVERY 1 MINUTE
        DO
			DELETE FROM Token WHERE TIMESTAMPDIFF(MINUTE, created_at, NOW()) > 1;
```
