

  CREATE TABLE IF NOT EXISTS sector(
  id VARCHAR(2) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,

  PRIMARY KEY (id)
  );
  
  CREATE TABLE IF NOT EXISTS n2(
  id VARCHAR(5) UNIQUE NOT NULL,
  nombre VARCHAR(64) NOT NULL,
  sector_id VARCHAR(2) NOT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (sector_id) REFERENCES sector(id)
  );
  
  CREATE TABLE IF NOT EXISTS n3(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(64) NOT NULL,
  sector_id VARCHAR(2) NOT NULL,
  n2_id VARCHAR(5) NOT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (sector_id) REFERENCES sector(id),
  FOREIGN KEY (n2_id) REFERENCES n2(id)
  );
  
  CREATE TABLE IF NOT EXISTS n4(
  id VARCHAR(11) UNIQUE NOT NULL,
  nombre VARCHAR(64) NOT NULL,
  sector_id VARCHAR(2) NOT NULL,
  n2_id VARCHAR(5) NOT NULL,
  n3_id VARCHAR(8) NOT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (sector_id) REFERENCES sector(id),
  FOREIGN KEY (n2_id) REFERENCES n2(id),
  FOREIGN KEY (n3_id) REFERENCES n3(id)
  );

  CREATE TABLE IF NOT EXISTS centro(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,

  PRIMARY KEY (id)
  );

  CREATE TABLE IF NOT EXISTS marca(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,

  PRIMARY KEY (id)
  );

  CREATE TABLE IF NOT EXISTS agrupado(
  id VARCHAR(32) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,

  PRIMARY KEY (id)
  );

  CREATE TABLE IF NOT EXISTS tipoEnvasado(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(16) NOT NULL,

  PRIMARY KEY (id)
  );
  
  CREATE TABLE IF NOT EXISTS estadoRefrigerado(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(16) NOT NULL,

  PRIMARY KEY (id)
  );
  
  
  CREATE TABLE IF NOT EXISTS material(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(64) NOT NULL,
  fechaCreacion DATE NOT NULL,
  duracion INT DEFAULT 0,
  pesoCaja FLOAT DEFAULT 0,
  activo INT NOT NULL DEFAULT 0,
  tipoEnvasado_id VARCHAR(8) DEFAULT NULL,
  estadoRefrigerado_id VARCHAR(8) DEFAULT NULL,
  sector_id VARCHAR(2) NOT NULL,
  marca_id VARCHAR(8) DEFAULT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (tipoEnvasado_id) REFERENCES tipoEnvasado(id),
  FOREIGN KEY (estadoRefrigerado_id) REFERENCES estadoRefrigerado(id),
  FOREIGN KEY (sector_id) REFERENCES sector(id),
  FOREIGN KEY (marca_id) REFERENCES marca(id)
  );
  
  CREATE TABLE IF NOT EXISTS oficinaVentas(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,
  centro_id VARCHAR(8) NOT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (centro_id) REFERENCES centro(id)
  );