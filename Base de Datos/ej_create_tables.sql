CREATE TABLE IF NOT EXISTS zona(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(32) UNIQUE NOT NULL,
  PRIMARY KEY (id)
  );
  
  CREATE TABLE IF NOT EXISTS cliente(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(64) NOT NULL,
  categoria VARCHAR(64),
  subtipo VARCHAR(64),

  PRIMARY KEY (id)
  );