Delimiter $$

DROP PROCEDURE IF EXISTS sp_generate_reporte_disponibilidad $$

CREATE PROCEDURE sp_generate_reporte_disponibilidad (
  IN fechaInicio VARCHAR(10), 
  IN fechaFin VARCHAR(10)
)
BEGIN

	DROP TEMPORARY TABLE IF EXISTS tableBase;
	DROP TEMPORARY TABLE IF EXISTS tb;
	DROP TEMPORARY TABLE IF EXISTS tablaDisponibilidad;

	CREATE TEMPORARY TABLE IF NOT EXISTS tableBase AS
	(
	SELECT 
		pedido.centro_id AS centro_id,
		material.sector_id AS sector_id,
		material.agrupado_id AS agrupado_id, 
		pedido.fechaEntrega AS fecha,
		SUM(pedido_material.cantidadCj) AS pedido_Cj,
		SUM(pedido_material.pesoKg) AS pedido_Kg,
		SUM(pedido_material.precioNeto) AS pedido_neto,
		0 AS disponible_Cj,
		0 AS disponible_Kg,
		0 AS despacho_Cj,
		0 AS despacho_Kg
	FROM 
		pedido, pedido_material, material
	WHERE 
		(pedido.fechaEntrega BETWEEN fechaInicio AND fechaFin) 
        AND pedido.id = pedido_material.pedido_id 
		AND pedido_material.material_id = material.id
	GROUP BY 
		material.agrupado_id, pedido.fechaEntrega, pedido.centro_id
	);

	INSERT INTO tableBase (centro_id,sector_id,agrupado_id,fecha,pedido_Cj,pedido_Kg,pedido_neto,disponible_Cj,
	disponible_Kg,despacho_Cj,despacho_Kg)
	(
	SELECT 
		despacho.centro_id,
		material.sector_id,
		material.agrupado_id,
		despacho.fecha,
		0, 0, 0, 0, 0,
		TRUNCATE(SUM(despacho_material.despachoCj),1), 
		TRUNCATE(SUM(despacho_material.despachoKg),3)
	FROM
		despacho, despacho_material, material
	WHERE 
		(despacho.fecha BETWEEN fechaInicio AND fechaFin)
		AND despacho.id = despacho_material.despacho_id
		AND despacho_material.material_id = material.id
	GROUP BY
		material.agrupado_id, despacho.fecha, despacho.centro_id
	);

	INSERT INTO tableBase (centro_id,sector_id,agrupado_id,fecha,pedido_Cj,pedido_Kg,pedido_neto,disponible_Cj,
	disponible_Kg,despacho_Cj,despacho_Kg)
	(
	SELECT 
		(if(stock.centro_id='T011' OR stock.centro_id='T018','T005',stock.centro_id)),
		material.sector_id,
		material.agrupado_id,
		stock.fecha, 
		0, 0, 0, 
		TRUNCATE(SUM(((IF(stock.stock<0,0,stock.stock))+stock.salidas)/material.pesoCaja),1),
		TRUNCATE(SUM((IF(stock.stock<0,0,stock.stock))+stock.salidas), 3), 0, 0
	FROM 
		stock, material
	WHERE 
    (stock.fecha BETWEEN fechaInicio AND fechaFin)
		AND stock.material_id = material.id
	GROUP BY
		material.agrupado_id, stock.fecha, stock.centro_id
	);

	CREATE TEMPORARY TABLE IF NOT EXISTS tb AS
	(
	SELECT 
		centro_id,
		centro.nombre AS centro_nombre,
		sector_id, sector.nombre AS sector_nombre, 
		agrupado_id,
		agrupado.nombre AS agrupado_nombre,
		fecha, SUM(pedido_Cj) AS pedido_Cj,
		SUM(pedido_Kg) AS pedido_Kg, 
		SUM(pedido_neto) AS pedido_neto, 
		SUM(disponible_Cj) AS disponible_Cj, 
		SUM(disponible_Kg) AS disponible_Kg, 
		SUM(despacho_Cj) AS despacho_Cj, 
		SUM(despacho_Kg) AS despacho_Kg
	FROM 
		tableBase, agrupado, centro, sector
	WHERE 
		tableBase.centro_id = centro.id 
		AND tableBase.sector_id = sector.id 
		AND tableBase.agrupado_id = agrupado.id
	GROUP BY 
		agrupado_id, centro_id, fecha
	);

	CREATE TEMPORARY TABLE IF NOT EXISTS tablaDisponibilidad AS
	(
	SELECT 
		tb.centro_id, 
		REPLACE(tb.centro_nombre,"Sucursal ","") AS centro_nombre, 
		tb.sector_id, tb.sector_nombre, 
		tb.agrupado_id, 
		tb.agrupado_nombre,	
		tb.fecha, 
		TRUNCATE(tb.pedido_Cj,1) AS pedido_Cj, 
		TRUNCATE(tb.despacho_Cj,1) AS despacho_Cj, 
		TRUNCATE(tb.disponible_Cj,1) AS disponible_Cj, 
		TRUNCATE(tb.pedido_Kg,3) AS pedido_Kg, 
		TRUNCATE(tb.pedido_neto,0) AS pedido_neto, 
		TRUNCATE(tb.disponible_Kg,3) AS disponible_Kg, 
		TRUNCATE(IF(tb.disponible_Cj<tb.pedido_Cj,tb.pedido_Cj-tb.disponible_Cj,0),1) AS faltante_Cj,
		TRUNCATE(IF(tb.disponible_Kg<tb.pedido_Kg,tb.pedido_Kg-tb.disponible_Kg,0),3) AS faltante_Kg,
		WEEK(tb.fecha,3) AS semana,
		TRUNCATE(IF(tb.disponible_Cj>tb.pedido_Cj,tb.disponible_Cj-tb.pedido_Cj,0),1) AS sobrante_Cj,
		TRUNCATE(IF(tb.disponible_Kg>tb.pedido_Kg,tb.disponible_Kg-tb.pedido_Kg,0),3) AS sobrante_Kg,
		TRUNCATE(IF(tb.despacho_Cj<tb.pedido_Cj,tb.pedido_Cj-tb.despacho_Cj,0),1) AS faltanteDespacho_Cj,
		TRUNCATE(LEAST(IF(tb.despacho_Cj<tb.pedido_Cj,tb.pedido_Cj-tb.despacho_Cj,0),
		IF(tb.disponible_Cj<tb.pedido_Cj,tb.pedido_Cj-tb.disponible_Cj,0)),1) AS faltanteAjustado_Cj,
		TRUNCATE(IF(tb.despacho_Kg<tb.pedido_Kg,tb.pedido_Kg-tb.despacho_Kg,0),3) AS faltanteDespacho_Kg,
		TRUNCATE(LEAST(IF(tb.despacho_Kg<tb.pedido_Kg,tb.pedido_Kg-tb.despacho_Kg,0),
		IF(tb.disponible_Kg<tb.pedido_Kg,tb.pedido_Kg-tb.disponible_Kg,0)),3) AS faltanteAjustado_Kg,
		WEEKDAY(tb.fecha)+1 AS diaSemana,
		YEAR(tb.fecha) AS año
	FROM
		tb
	);
    
END 
$$

DROP PROCEDURE IF EXISTS sp_reporte_disponibilidad $$

CREATE PROCEDURE sp_reporte_disponibilidad (
  IN fechaInicio VARCHAR(10), 
  IN fechaFin VARCHAR(10)
)
BEGIN
	CALL sp_generate_reporte_disponibilidad(fechaInicio,fechaFin);
	SELECT * FROM tablaDisponibilidad;
END
$$

DROP PROCEDURE IF EXISTS sp_generate_reporte_arbol_perdidas $$

CREATE PROCEDURE sp_generate_reporte_arbol_perdidas (
  IN fechaInicio VARCHAR(10), 
  IN fechaFin VARCHAR(10)
)
BEGIN
	CALL sp_generate_reporte_disponibilidad(fechaInicio,fechaFin);
                        
	DROP TEMPORARY TABLE IF EXISTS tabla_arbol_perdidas;
    
    CREATE TEMPORARY TABLE IF NOT EXISTS tabla_arbol_perdidas(
		mes INT,
		semana INT,
		sector_nombre VARCHAR(32),
		tipoCliente VARCHAR(32),
		centro_id VARCHAR(8),
		centro_nombre VARCHAR(32),
		agrupado_id VARCHAR(32),
		agrupado_nombre VARCHAR(32),
		n2_nombre VARCHAR(32),
		Pedido_Kg FLOAT, 
		Factura_Kg FLOAT, 
		Demanda_Kg FLOAT, 
		NS_Kg FLOAT, 
		Faltante_Kg FLOAT, 
		Sobrefactura_Kg FLOAT,  
		PP_Neto INT,  
		Faltante_Neto INT, 
		Pedido_Cj FLOAT, 
		Factura_Cj FLOAT,  
		Demanda_Cj FLOAT,  
		NS_Cj FLOAT,  
		Sobrefactura_Cj FLOAT,  
		Faltante_Cj FLOAT, 
		Disp_Pedido_Cj FLOAT, 
		Disp_Faltante_Cj FLOAT, 
		Disp_Pedido_Kg FLOAT, 
		Disp_Faltante_Kg FLOAT,  
		Factura_Faltante_Kg FLOAT, 
		Factura_Faltante_Cj FLOAT, 
		Pedido_Neto INT,
		Año INT,
		semanaAño VARCHAR(8)
	  );
      
	INSERT INTO tabla_arbol_perdidas (mes,semana,sector_nombre,tipoCliente,centro_id,centro_nombre,agrupado_id,agrupado_nombre,
    n2_nombre,Pedido_Kg, Factura_Kg, Demanda_Kg, NS_Kg, Faltante_Kg,Sobrefactura_Kg, PP_Neto, Faltante_Neto, 
    Pedido_Cj, Factura_Cj, Demanda_Cj, NS_Cj, Sobrefactura_Cj, Faltante_Cj, Disp_Pedido_Cj, Disp_Faltante_Cj,
    Disp_Pedido_Kg, Disp_Faltante_Kg, Factura_Faltante_Kg,Factura_Faltante_Cj,Pedido_Neto,Año,semanaAño)
	(   
	SELECT 
		MONTH(tablaDisponibilidad.fecha),
        WEEK(tablaDisponibilidad.fecha,3),
		tablaDisponibilidad.sector_nombre,
		"",
		tablaDisponibilidad.centro_id, 
		REPLACE(tablaDisponibilidad.centro_nombre,"Sucursal ",""),
		tablaDisponibilidad.agrupado_id, 
		tablaDisponibilidad.agrupado_nombre,
		n2.nombre,
		0,0,0,0,0,0,0,0,0,0,0,0,0,0, 
		tablaDisponibilidad.pedido_Cj,
		tablaDisponibilidad.faltante_Cj, 
		tablaDisponibilidad.pedido_Kg,
		tablaDisponibilidad.faltante_Kg,
		0, 0,
		tablaDisponibilidad.pedido_neto, 
		tablaDisponibilidad.año,
		CONCAT(tablaDisponibilidad.semana, '-', tablaDisponibilidad.año)
    FROM 
		tablaDisponibilidad, agrupado, n2
    WHERE 
		agrupado.id = tablaDisponibilidad.agrupado_id AND n2.id = agrupado.n2_id
	);	
    
	INSERT INTO tabla_arbol_perdidas (mes,semana,sector_nombre,tipoCliente,centro_id,centro_nombre,agrupado_id,agrupado_nombre,
    n2_nombre,Pedido_Kg, Factura_Kg, Demanda_Kg, NS_Kg, Faltante_Kg,Sobrefactura_Kg, PP_Neto, Faltante_Neto, 
    Pedido_Cj, Factura_Cj, Demanda_Cj, NS_Cj, Sobrefactura_Cj, Faltante_Cj, Disp_Pedido_Cj, Disp_Faltante_Cj,
    Disp_Pedido_Kg, Disp_Faltante_Kg, Factura_Faltante_Kg,Factura_Faltante_Cj,Pedido_Neto,Año,semanaAño)
	(    
	SELECT 
		MONTH(faltante.fecha),
		WEEK(faltante.fecha,3),
        sector.nombre,
		tipoCliente.nombre,
		faltante.centro_id,
		REPLACE(centro.nombre,"Sucursal ",""),
		material.agrupado_id,
		agrupado.nombre,
        n2.nombre,
        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		SUM(faltante.faltanteKg),
		SUM(faltante.faltanteCj),
		0,
        YEAR(faltante.fecha),
		CONCAT(WEEK(faltante.fecha,3), '-', YEAR(faltante.fecha))
	FROM 
		faltante, centro, material, sector, agrupado, n2, clienteLocal, cliente, tipoCliente
    WHERE 
		(faltante.fecha BETWEEN fechaInicio AND fechaFin) 
		AND faltante.centro_id = centro.id 
		AND faltante.material_id = material.id
		AND material.agrupado_id = agrupado.id 
		AND material.sector_id = sector.id 
		AND agrupado.n2_id = n2.id 
		AND faltante.clienteLocal_id = clienteLocal.id 
		AND clienteLocal.cliente_id = cliente.id
		AND cliente.tipoCliente_id = tipoCliente.id
	GROUP BY 
		MONTH(faltante.fecha), WEEK(faltante.fecha,3), material.agrupado_id, faltante.centro_id
	);		
	
	INSERT INTO tabla_arbol_perdidas (mes,semana,sector_nombre,tipoCliente,centro_id,centro_nombre,agrupado_id,agrupado_nombre,
    n2_nombre,Pedido_Kg, Factura_Kg, Demanda_Kg, NS_Kg, Faltante_Kg,Sobrefactura_Kg, PP_Neto, Faltante_Neto, 
    Pedido_Cj, Factura_Cj, Demanda_Cj, NS_Cj, Sobrefactura_Cj, Faltante_Cj, Disp_Pedido_Cj, Disp_Faltante_Cj,
    Disp_Pedido_Kg, Disp_Faltante_Kg, Factura_Faltante_Kg,Factura_Faltante_Cj,Pedido_Neto,Año,semanaAño)
	(    
	SELECT 
		MONTH(ns_cliente.fecha),
		WEEK(ns_cliente.fecha,3),
        sector.nombre,
		tipoCliente.nombre,
		ns_cliente.centro_id,
		REPLACE(centro.nombre,"Sucursal ",""),
		ns_cliente.agrupado_id,
		agrupado.nombre,
        n2.nombre,
        ns_cliente.pedidoKg,
        ns_cliente.facturaKg,
        ns_cliente.demandaKg,
        ns_cliente.nsKg,
        ns_cliente.faltanteKg,
        ns_cliente.sobrefacturaKg,
        ns_cliente.ppNeto,
        ns_cliente.faltanteNeto,
        ns_cliente.pedidoCj,
        ns_cliente.facturaCj,
        ns_cliente.demandaCj,
        ns_cliente.nsCj,
        ns_cliente.sobrefacturaCj,
        ns_cliente.faltanteCj,
        0,0,0,0,0,0,0,
        YEAR(ns_cliente.fecha),
		CONCAT(WEEK(ns_cliente.fecha,3), '-', YEAR(ns_cliente.fecha))
	FROM 
		ns_cliente, centro, agrupado, n2, sector, clienteLocal, cliente, tipoCliente
    WHERE 
		(ns_cliente.fecha BETWEEN fechaInicio AND fechaFin) 
		AND ns_cliente.centro_id = centro.id
		AND ns_cliente.agrupado_id = agrupado.id 
		AND agrupado.n2_id = n2.id 
		AND n2.sector_id = sector.id 
		AND ns_cliente.clienteLocal_id = clienteLocal.id 
		AND clienteLocal.cliente_id = cliente.id
		AND cliente.tipoCliente_id = tipoCliente.id
	);		
    
END 
$$

DROP PROCEDURE IF EXISTS sp_reporte_arbol_perdidas $$

CREATE PROCEDURE sp_reporte_arbol_perdidas (
  IN fechaInicio VARCHAR(10), 
  IN fechaFin VARCHAR(10)
)
BEGIN
	CALL sp_generate_reporte_arbol_perdidas(fechaInicio,fechaFin);
	SELECT * FROM tabla_arbol_perdidas;
END
$$

/*
CALL sp_reporte_disponibilidad('2018-01-30','2018-01-30');
CALL sp_reporte_arbol_perdidas('2018-01-30','2018-01-30');
*/

