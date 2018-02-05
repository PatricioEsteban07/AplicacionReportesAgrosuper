
  CREATE TABLE IF NOT EXISTS region(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,
  pais VARCHAR(8) NOT NULL,

  PRIMARY KEY (id)
  );
  
  CREATE TABLE IF NOT EXISTS centro(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,
  region_id VARCHAR(8),

  PRIMARY KEY (id),
  
  FOREIGN KEY (region_id) REFERENCES region(id)
  );
  
  CREATE TABLE IF NOT EXISTS zonaVentas(
  id VARCHAR(4) UNIQUE NOT NULL,
  nombre VARCHAR(16) NOT NULL,

  PRIMARY KEY (id)
  );

  CREATE TABLE IF NOT EXISTS sector(
  id VARCHAR(2) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,

  PRIMARY KEY (id),
  
  UNIQUE INDEX idx_sector_id USING BTREE (id) 
  );
  
  CREATE TABLE IF NOT EXISTS n2(
  id VARCHAR(6) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,
  sector_id VARCHAR(2) NOT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (sector_id) REFERENCES sector(id),
  
  UNIQUE INDEX idx_n2_id USING BTREE (id) 
  );
  
  CREATE TABLE IF NOT EXISTS n3(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(64) NOT NULL,
  n2_id VARCHAR(6) NOT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (n2_id) REFERENCES n2(id),
  
  UNIQUE INDEX idx_n3_id USING BTREE (id) 
  );
  
  CREATE TABLE IF NOT EXISTS n4(
  id VARCHAR(12) UNIQUE NOT NULL,
  nombre VARCHAR(64) NOT NULL,
  n3_id VARCHAR(8) NOT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (n3_id) REFERENCES n3(id),
  
  UNIQUE INDEX idx_n4_id USING BTREE (id) 
  );
  
  CREATE TABLE IF NOT EXISTS tipoCliente(
  id VARCHAR(4) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,

  PRIMARY KEY (id)
  );

  CREATE TABLE IF NOT EXISTS marca(
  id VARCHAR(4) UNIQUE NOT NULL,
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
  id VARCHAR(2) UNIQUE NOT NULL,
  nombre VARCHAR(16) NOT NULL,

  PRIMARY KEY (id)
  );
  
  CREATE TABLE IF NOT EXISTS estadoRefrigerado(
  id VARCHAR(2) UNIQUE NOT NULL,
  nombre VARCHAR(16) NOT NULL,

  PRIMARY KEY (id)
  );
  
  CREATE TABLE IF NOT EXISTS material(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(64) NOT NULL,
  fechaCreacion DATE,
  pesoCaja FLOAT DEFAULT 0,
  sector_id VARCHAR(2) DEFAULT NULL,
  duracion INT DEFAULT 0,
  estadoRefrigerado_id VARCHAR(2) DEFAULT NULL,
  agrupado_id VARCHAR(32) DEFAULT NULL,
  tipoEnvasado_id VARCHAR(2) DEFAULT NULL,
  marca_id VARCHAR(4) DEFAULT NULL,

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
  zonaVentas_id VARCHAR(4) NOT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (zonaVentas_id) REFERENCES zonaVentas(id),
  
  UNIQUE INDEX idx_oficinaVentas_id USING BTREE (id) 
  );
  
  CREATE TABLE IF NOT EXISTS cliente(
  id VARCHAR(16) NOT NULL,
  nombre VARCHAR(64) NOT NULL,
  direccion VARCHAR(32) NOT NULL,
  region_id VARCHAR(8) NOT NULL,
  fechaCreacion DATE,
  tipoCliente_id VARCHAR(4) DEFAULT NULL,

  PRIMARY KEY (id), 
  
  FOREIGN KEY (region_id) REFERENCES region(id),
  FOREIGN KEY (tipoCliente_id) REFERENCES tipoCliente(id)
  );
  
  CREATE TABLE IF NOT EXISTS clienteLocal(
  id VARCHAR(16) NOT NULL,
  nombre VARCHAR(64) NOT NULL,
  direccion VARCHAR(32) NOT NULL,
  region_id VARCHAR(8) NOT NULL,
  cliente_id VARCHAR(16) DEFAULT NULL,

  PRIMARY KEY (id), 
  
  FOREIGN KEY (region_id) REFERENCES region(id),
  FOREIGN KEY (cliente_id) REFERENCES cliente(id)
  );
  
  CREATE TABLE IF NOT EXISTS pedido(
  id VARCHAR(16) UNIQUE NOT NULL,
  fechaDoc DATE NOT NULL,
  centro_id VARCHAR(8) NOT NULL,
  oficina_id VARCHAR(8) NOT NULL,
  tipoCliente_id VARCHAR(4) NOT NULL,
  fechaEntrega DATE NOT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (centro_id) REFERENCES centro(id),
  FOREIGN KEY (oficina_id) REFERENCES oficinaVentas(id),
  FOREIGN KEY (tipoCliente_id) REFERENCES tipoCliente(id)
  );
    
  CREATE TABLE IF NOT EXISTS pedido_material(
  pedido_id VARCHAR(16) NOT NULL,
  material_id VARCHAR(8) NOT NULL,
  cantidadCj INT DEFAULT 0,
  pesoKg FLOAT DEFAULT 0,
  precioNeto INT DEFAULT 0,

  PRIMARY KEY (pedido_id,material_id),
  
  FOREIGN KEY (pedido_id) REFERENCES pedido(id),
  FOREIGN KEY (material_id) REFERENCES material(id)
  );
  
  CREATE TABLE IF NOT EXISTS stock(
  centro_id VARCHAR(8) NOT NULL,
  fecha DATE NOT NULL,
  material_id VARCHAR(8) NOT NULL,
  salidas FLOAT DEFAULT 0,
  stock FLOAT DEFAULT 0,
  disponible FLOAT DEFAULT 0,

  PRIMARY KEY (centro_id,fecha,material_id),
  
  FOREIGN KEY (centro_id) REFERENCES centro(id),
  FOREIGN KEY (material_id) REFERENCES material(id)
  );
  
  CREATE TABLE IF NOT EXISTS despacho(
  id VARCHAR(16) NOT NULL,
  centro_id VARCHAR(8) NOT NULL,
  fecha DATE NOT NULL,
  clienteLocal_id VARCHAR(16) NOT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (centro_id) REFERENCES centro(id),
  FOREIGN KEY (clienteLocal_id) REFERENCES clienteLocal(id)
  );
  
  CREATE TABLE IF NOT EXISTS despacho_material(
  despacho_id VARCHAR(16) NOT NULL,
  material_id VARCHAR(8) NOT NULL,
  despachoCj float DEFAULT 0,
  despachoKg float DEFAULT 0,

  PRIMARY KEY (despacho_id,material_id),
  
  FOREIGN KEY (despacho_id) REFERENCES despacho(id),
  FOREIGN KEY (material_id) REFERENCES material(id)
  );