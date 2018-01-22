
  
  /*PERSONAL Y CLIENTES*/
  
  CREATE TABLE IF NOT EXISTS cliente(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(64) NOT NULL,
  categoria_id VARCHAR(8),
  subtipo_id VARCHAR(8),

  PRIMARY KEY (id)
  );
  
  /*ZONAS, SUCURSALES*/
  CREATE TABLE IF NOT EXISTS zona(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(32) UNIQUE NOT NULL,
  PRIMARY KEY (id)
  );
  
  CREATE TABLE IF NOT EXISTS sucursal(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,
  categoria VARCHAR(64),
  subtipo VARCHAR(64),
  zona_id VARCHAR(8) NOT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (zona_id) REFERENCES zona(id)
  );
  
  
  /*ALIMENTOS*/
  
  CREATE TABLE IF NOT EXISTS marca_producto(
  id VARCHAR(4) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,

  PRIMARY KEY (id)
  );
