
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

  PRIMARY KEY (id)
  );
  
  CREATE TABLE IF NOT EXISTS n2(
  id VARCHAR(6) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,
  sector_id VARCHAR(2) NOT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (sector_id) REFERENCES sector(id)
  );
  
  CREATE TABLE IF NOT EXISTS n3(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(64) NOT NULL,
  n2_id VARCHAR(6) NOT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (n2_id) REFERENCES n2(id)
  );
  
  CREATE TABLE IF NOT EXISTS n4(
  id VARCHAR(12) UNIQUE NOT NULL,
  nombre VARCHAR(64) NOT NULL,
  n3_id VARCHAR(8) NOT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (n3_id) REFERENCES n3(id)
  );
  
  CREATE TABLE IF NOT EXISTS tipoCliente(
  id VARCHAR(4) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,

  PRIMARY KEY (id)
  );
  
  CREATE TABLE IF NOT EXISTS categoriaCliente(
  id VARCHAR(4) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,
  tipoCliente_id VARCHAR(4) NOT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (tipoCliente_id) REFERENCES tipoCliente(id)
  );
  
  CREATE TABLE IF NOT EXISTS subcategoriaCliente(
  id VARCHAR(4) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,
  categoriaCliente_id VARCHAR(4) NOT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (categoriaCliente_id) REFERENCES categoriaCliente(id)
  );

  CREATE TABLE IF NOT EXISTS marca(
  id VARCHAR(4) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,

  PRIMARY KEY (id)
  );

  CREATE TABLE IF NOT EXISTS agrupado(
  id VARCHAR(32) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,
  n2_id VARCHAR(6) NOT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (n2_id) REFERENCES n2(id)
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
  
  CREATE TABLE IF NOT EXISTS oficinaVentas(
  id VARCHAR(8) UNIQUE NOT NULL,
  nombre VARCHAR(32) NOT NULL,
  zonaVentas_id VARCHAR(4) NOT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (zonaVentas_id) REFERENCES zonaVentas(id)
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
  n4_id VARCHAR(12) DEFAULT NULL,

  PRIMARY KEY (id),
  
  FOREIGN KEY (tipoEnvasado_id) REFERENCES tipoEnvasado(id),
  FOREIGN KEY (estadoRefrigerado_id) REFERENCES estadoRefrigerado(id),
  FOREIGN KEY (agrupado_id) REFERENCES agrupado(id),
  FOREIGN KEY (sector_id) REFERENCES sector(id),
  FOREIGN KEY (marca_id) REFERENCES marca(id),
  FOREIGN KEY (n4_id) REFERENCES n4(id)
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
  comuna VARCHAR(24) NOT NULL,
  cliente_id VARCHAR(16) DEFAULT NULL,
  subCategoriaCliente_id VARCHAR(4) NOT NULL,
  tipoClub VARCHAR(16) DEFAULT NULL,
  tipoCallCenter VARCHAR(16) DEFAULT NULL,

  PRIMARY KEY (id), 
  
  FOREIGN KEY (region_id) REFERENCES region(id),
  FOREIGN KEY (cliente_id) REFERENCES cliente(id),
  FOREIGN KEY (subCategoriaCliente_id) REFERENCES subcategoriaCliente(id)
  );
  
  CREATE TABLE IF NOT EXISTS clubCliente(
  cliente_id VARCHAR(16) NOT NULL,
  categoria VARCHAR(16) DEFAULT "SIN DATO",
  segmento VARCHAR(32) DEFAULT "SIN DATO",
  tipoCliente_id VARCHAR(4) NOT NULL,
  zona_id VARCHAR(4) NOT NULL,
  sucursal VARCHAR(32) NOT NULL,
  canje VARCHAR(4) DEFAULT "NO",

  PRIMARY KEY (cliente_id), 
  
  FOREIGN KEY (cliente_id) REFERENCES cliente(id),
  FOREIGN KEY (tipoCliente_id) REFERENCES tipoCliente(id),
  FOREIGN KEY (zona_id) REFERENCES zonaventas(id)
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
  cantidadCj FLOAT DEFAULT 0,
  pesoKg FLOAT DEFAULT 0,
  precioNeto FLOAT DEFAULT 0,

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
  
  CREATE TABLE IF NOT EXISTS despacho_faltante(
  centro_id VARCHAR(8) NOT NULL,
  fecha DATE NOT NULL,
  clienteLocal_id VARCHAR(16) NOT NULL,
  material_id VARCHAR(8) NOT NULL,
  despachoKg FLOAT DEFAULT 0,
  faltanteKg FLOAT DEFAULT 0,
  despachoCj FLOAT DEFAULT 0,
  faltanteCj FLOAT DEFAULT 0,

  PRIMARY KEY (centro_id,fecha,clienteLocal_id,material_id),
  
  FOREIGN KEY (centro_id) REFERENCES centro(id),
  FOREIGN KEY (clienteLocal_id) REFERENCES clienteLocal(id),
  FOREIGN KEY (material_id) REFERENCES material(id)
  );
  
  CREATE TABLE IF NOT EXISTS ns_cliente(
  fecha DATE NOT NULL,
  centro_id VARCHAR(8) NOT NULL,
  oficina_id VARCHAR(8) NOT NULL,
  clienteLocal_id VARCHAR(16) NOT NULL,
  agrupado_id VARCHAR(32) NOT NULL,
  pedidoKg FLOAT DEFAULT 0,
  facturaKg FLOAT DEFAULT 0,
  demandaKg FLOAT DEFAULT 0,
  nsKg FLOAT DEFAULT 0,
  faltanteKg FLOAT DEFAULT 0,
  sobrefacturaKg FLOAT DEFAULT 0,
  ppNeto INT DEFAULT 0,
  faltanteNeto INT DEFAULT 0,
  pedidoCj FLOAT DEFAULT 0,
  facturaCj FLOAT DEFAULT 0,
  demandaCj FLOAT DEFAULT 0,
  nsCj FLOAT DEFAULT 0,
  sobrefacturaCj FLOAT DEFAULT 0,
  faltanteCj FLOAT DEFAULT 0,
  
  PRIMARY KEY (clienteLocal_id,agrupado_id,oficina_id,fecha),
  
  FOREIGN KEY (centro_id) REFERENCES centro(id),
  FOREIGN KEY (oficina_id) REFERENCES oficinaVentas(id),
  FOREIGN KEY (clienteLocal_id) REFERENCES clienteLocal(id),
  FOREIGN KEY (agrupado_id) REFERENCES agrupado(id)
  );
  
  CREATE TABLE IF NOT EXISTS venta_FS(
  clienteLocal_id VARCHAR(16) NOT NULL,
  tipoCliente_id VARCHAR(4) NOT NULL,
  subcategoriaCliente_id VARCHAR(4) NOT NULL,
  año INT NOT NULL,
  mes INT NOT NULL,
  oficina_id VARCHAR(8) NOT NULL,
  agcnc VARCHAR(32) NOT NULL,
  sector_id VARCHAR(2) NOT NULL,
  venta_Kg FLOAT NOT NULL,
  venta_Neta INT NOT NULL,

  PRIMARY KEY (clienteLocal_id,año,mes,oficina_id,sector_id),
  
  FOREIGN KEY (clienteLocal_id) REFERENCES clienteLocal(id),
  FOREIGN KEY (tipoCliente_id) REFERENCES tipoCliente(id),
  FOREIGN KEY (subcategoriaCliente_id) REFERENCES subcategoriaCliente(id),
  FOREIGN KEY (oficina_id) REFERENCES oficinaventas(id),
  FOREIGN KEY (sector_id) REFERENCES sector(id)
  );
  
  