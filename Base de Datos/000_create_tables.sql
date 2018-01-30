

  CREATE TABLE IF NOT EXISTS sector(
  id VARCHAR(2) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,

  PRIMARY KEY (id),
  
  UNIQUE INDEX idx_sector_id USING BTREE (id) 
  );
  
  CREATE TABLE IF NOT EXISTS n2(
  id VARCHAR(5) UNIQUE NOT NULL,
  nombre VARCHAR(64) NOT NULL,
  sector_id VARCHAR(2) NOT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (sector_id) REFERENCES sector(id),
  
  UNIQUE INDEX idx_n2_id USING BTREE (id) 
  );
  
  CREATE TABLE IF NOT EXISTS n3(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(64) NOT NULL,
  sector_id VARCHAR(2) NOT NULL,
  n2_id VARCHAR(5) NOT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (sector_id) REFERENCES sector(id),
  FOREIGN KEY (n2_id) REFERENCES n2(id),
  
  UNIQUE INDEX idx_n3_id USING BTREE (id) 
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
  FOREIGN KEY (n3_id) REFERENCES n3(id),
  
  UNIQUE INDEX idx_n4_id USING BTREE (id) 
  );

  CREATE TABLE IF NOT EXISTS centro(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,

  PRIMARY KEY (id)
  );

  CREATE TABLE IF NOT EXISTS marca(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,

  PRIMARY KEY (id),
  
  UNIQUE INDEX idx_marca_id USING BTREE (id) 
  );

  CREATE TABLE IF NOT EXISTS agrupado(
  id VARCHAR(32) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,

  PRIMARY KEY (id),
  
  UNIQUE INDEX idx_agrupado_id USING BTREE (id) 
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
  fechaCreacion DATE,
  duracion INT DEFAULT 0,
  pesoCaja FLOAT DEFAULT 0,
  activo INT DEFAULT 0,
  tipoEnvasado_id VARCHAR(8) DEFAULT NULL,
  estadoRefrigerado_id VARCHAR(8) DEFAULT NULL,
  agrupado_id VARCHAR(32) DEFAULT NULL,
  sector_id VARCHAR(2) DEFAULT NULL,
  marca_id VARCHAR(8) DEFAULT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (tipoEnvasado_id) REFERENCES tipoEnvasado(id),
  FOREIGN KEY (estadoRefrigerado_id) REFERENCES estadoRefrigerado(id),
  FOREIGN KEY (agrupado_id) REFERENCES agrupado(id),
  FOREIGN KEY (sector_id) REFERENCES sector(id),
  FOREIGN KEY (marca_id) REFERENCES marca(id),
  
  UNIQUE INDEX idx_material_id USING BTREE (id) 
  );
  
  CREATE TABLE IF NOT EXISTS oficinaVentas(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,
  centro_id VARCHAR(8) NOT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (centro_id) REFERENCES centro(id),
  
  UNIQUE INDEX idx_oficinaVentas_id USING BTREE (id) 
  );
  
  CREATE TABLE IF NOT EXISTS cliente(
  id VARCHAR(16) NOT NULL,
  nombre VARCHAR(64) NOT NULL,
  tipoCliente VARCHAR(32) DEFAULT NULL,

  PRIMARY KEY (id)
  );
  
  CREATE TABLE IF NOT EXISTS pedido(
  material_id VARCHAR(8) NOT NULL,
  fecha DATE NOT NULL,
  oficina_id VARCHAR(8) NOT NULL,
  tipoCliente VARCHAR(32) DEFAULT NULL,
  pedidoCj INT DEFAULT 0,
  pedidoKg FLOAT DEFAULT 0,
  pedidoNeto INT DEFAULT 0,

  PRIMARY KEY (material_id,fecha,oficina_id),
  
  FOREIGN KEY (material_id) REFERENCES material(id),
  FOREIGN KEY (oficina_id) REFERENCES oficinaVentas(id)
  );
  
  CREATE TABLE IF NOT EXISTS stock(
  material_id VARCHAR(8) NOT NULL,
  fecha DATE NOT NULL,
  centro_id VARCHAR(8) NOT NULL,
  salidas FLOAT DEFAULT 0,
  stock FLOAT DEFAULT 0,
  disponible FLOAT DEFAULT 0,

  PRIMARY KEY (material_id,fecha,centro_id),
  
  FOREIGN KEY (material_id) REFERENCES material(id),
  FOREIGN KEY (centro_id) REFERENCES centro(id)
  );
  
  CREATE TABLE IF NOT EXISTS despacho(
  material_id VARCHAR(8) NOT NULL,
  fecha DATE NOT NULL,
  centro_id VARCHAR(8) NOT NULL,
  cliente_id VARCHAR(16) NOT NULL,
  despachoKg INT DEFAULT 0,
  despachoCj INT DEFAULT 0,
  faltanteKg INT DEFAULT 0,
  faltanteCj INT DEFAULT 0,

  PRIMARY KEY (material_id,fecha,centro_id,cliente_id),
  
  FOREIGN KEY (material_id) REFERENCES material(id),
  FOREIGN KEY (centro_id) REFERENCES centro(id),
  FOREIGN KEY (cliente_id) REFERENCES cliente(id)
  );