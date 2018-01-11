CREATE TABLE IF NOT EXISTS cliente(
  id SERIAL NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(64) NOT NULL,
  apellido VARCHAR(64) NOT NULL,
  edad int NOT NULL,
  sexo VARCHAR(4) NOT NULL DEFAULT "M",
  descripcion VARCHAR(250),

  PRIMARY KEY (id)
  );
  
  CREATE TABLE IF NOT EXISTS empresa(
  id SERIAL NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(64) NOT NULL,
  direccion VARCHAR(128) NOT NULL,
  descripcion VARCHAR(250),

  PRIMARY KEY (id)
  );
  
  CREATE TABLE IF NOT EXISTS cliente_empresa(
  empresa_id BIGINT UNSIGNED NOT NULL,
  cliente_id BIGINT UNSIGNED NOT NULL,

  PRIMARY KEY (empresa_id,cliente_id),
  
  FOREIGN KEY (empresa_id) REFERENCES empresa(id),
  FOREIGN KEY (cliente_id) REFERENCES cliente(id)
  );
