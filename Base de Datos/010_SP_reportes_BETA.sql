Delimiter $$

/* REPORTE 1 */

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
		pedido.centro_id, pedido.fechaEntrega, material.agrupado_id
	);

	INSERT INTO tableBase (centro_id,sector_id,agrupado_id,fecha,pedido_Cj,pedido_Kg,pedido_neto,disponible_Cj,
	disponible_Kg,despacho_Cj,despacho_Kg)
	(
	SELECT 
		despacho_faltante.centro_id,
		material.sector_id,
		material.agrupado_id,
		despacho_faltante.fecha,
		0, 0, 0, 0, 0,
		ROUND(SUM(despacho_faltante.despachoCj),1), 
		ROUND(SUM(despacho_faltante.despachoKg),3)
	FROM
		despacho_faltante, material
	WHERE 
		(despacho_faltante.fecha BETWEEN fechaInicio AND fechaFin)
		AND despacho_faltante.material_id = material.id
	GROUP BY
		despacho_faltante.centro_id, despacho_faltante.fecha, material.agrupado_id
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
		ROUND(SUM(((IF(stock.stock<0,0,stock.stock))+stock.salidas)/material.pesoCaja),1),
		ROUND(SUM((IF(stock.stock<0,0,stock.stock))+stock.salidas), 3), 0, 0
	FROM 
		stock, material
	WHERE 
    (stock.fecha BETWEEN fechaInicio AND fechaFin)
		AND stock.material_id = material.id
	GROUP BY
		stock.centro_id, stock.fecha, material.agrupado_id
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
		centro_id, fecha, agrupado_id
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
		ROUND(tb.pedido_Cj,1) AS pedido_Cj, 
		ROUND(tb.despacho_Cj,1) AS despacho_Cj, 
		ROUND(tb.disponible_Cj,1) AS disponible_Cj,
		ROUND(tb.pedido_Kg,3) AS pedido_Kg, 
		ROUND(tb.pedido_neto,0) AS pedido_neto, 
		ROUND(tb.disponible_Kg,3) AS disponible_Kg, 
		ROUND(IF(tb.disponible_Cj<tb.pedido_Cj,tb.pedido_Cj-tb.disponible_Cj,0),1) AS faltante_Cj,
		ROUND(IF(tb.disponible_Kg<tb.pedido_Kg,tb.pedido_Kg-tb.disponible_Kg,0),3) AS faltante_Kg,
		WEEK(tb.fecha,3) AS semana,
		ROUND(IF(tb.disponible_Cj>tb.pedido_Cj,tb.disponible_Cj-tb.pedido_Cj,0),1) AS sobrante_Cj,
		ROUND(IF(tb.disponible_Kg>tb.pedido_Kg,tb.disponible_Kg-tb.pedido_Kg,0),3) AS sobrante_Kg,
		ROUND(IF(tb.despacho_Cj<tb.pedido_Cj,tb.pedido_Cj-tb.despacho_Cj,0),1) AS faltanteDespacho_Cj,
		ROUND(LEAST(IF(tb.despacho_Cj<tb.pedido_Cj,tb.pedido_Cj-tb.despacho_Cj,0),
		IF(tb.disponible_Cj<tb.pedido_Cj,tb.pedido_Cj-tb.disponible_Cj,0)),1) AS faltanteAjustado_Cj,
		ROUND(IF(tb.despacho_Kg<tb.pedido_Kg,tb.pedido_Kg-tb.despacho_Kg,0),3) AS faltanteDespacho_Kg,
		ROUND(LEAST(IF(tb.despacho_Kg<tb.pedido_Kg,tb.pedido_Kg-tb.despacho_Kg,0),
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

/* REPORTE 2 */

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
		MONTH(despacho_faltante.fecha),
		WEEK(despacho_faltante.fecha,3),
        sector.nombre,
		tipoCliente.nombre,
		despacho_faltante.centro_id,
		REPLACE(centro.nombre,"Sucursal ",""),
		material.agrupado_id,
		agrupado.nombre,
        n2.nombre,
        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
		SUM(despacho_faltante.faltanteKg),
		SUM(despacho_faltante.faltanteCj),
		0,
        YEAR(despacho_faltante.fecha),
		CONCAT(WEEK(despacho_faltante.fecha,3), '-', YEAR(despacho_faltante.fecha))
	FROM 
		despacho_faltante, centro, material, sector, agrupado, n2, clienteLocal, cliente, tipoCliente
    WHERE 
		(despacho_faltante.fecha BETWEEN fechaInicio AND fechaFin) 
		AND despacho_faltante.centro_id = centro.id 
		AND despacho_faltante.material_id = material.id
		AND material.agrupado_id = agrupado.id 
		AND material.sector_id = sector.id 
		AND agrupado.n2_id = n2.id 
		AND despacho_faltante.clienteLocal_id = clienteLocal.id 
		AND clienteLocal.cliente_id = cliente.id
		AND cliente.tipoCliente_id = tipoCliente.id
	GROUP BY 
        YEAR(despacho_faltante.fecha), WEEK(despacho_faltante.fecha,3), despacho_faltante.centro_id, material.agrupado_id, tipoCliente.nombre
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
        SUM(ns_cliente.pedidoKg),
        SUM(ns_cliente.facturaKg),
        SUM(ns_cliente.demandaKg),
        SUM(ns_cliente.nsKg),
        SUM(ns_cliente.faltanteKg),
        SUM(ns_cliente.sobrefacturaKg),
        SUM(ns_cliente.ppNeto),
        SUM(ns_cliente.faltanteNeto),
        SUM(ns_cliente.pedidoCj),
        SUM(ns_cliente.facturaCj),
        SUM(ns_cliente.demandaCj),
        SUM(ns_cliente.nsCj),
        SUM(ns_cliente.sobrefacturaCj),
        SUM(ns_cliente.faltanteCj),
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
	GROUP BY 
        YEAR(ns_cliente.fecha), WEEK(ns_cliente.fecha,3), ns_cliente.centro_id, ns_cliente.agrupado_id, tipoCliente.nombre
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
    /*
    SELECT centro_id, SUM(Factura_Faltante_Kg), SUM(Factura_Faltante_Cj) FROM tabla_arbol_perdidas 
    WHERE semanaAño = (CONCAT(WEEK(fechaInicio,3), '-', YEAR(fechaInicio))) GROUP BY centro_id;
    */
END
$$

/* REPORTE 3 */

DROP PROCEDURE IF EXISTS sp_generate_reporte_fuga_fs $$

CREATE PROCEDURE sp_generate_reporte_fuga_fs (
  IN fechaInicio VARCHAR(10), 
  IN fechaFin VARCHAR(10)
)
BEGIN                        
	DROP TEMPORARY TABLE IF EXISTS tabla_fuga_fs;
	DROP TEMPORARY TABLE IF EXISTS tableAuxFuga;
     
     
    CREATE TEMPORARY TABLE IF NOT EXISTS tabla_fuga_fs(
		clienteLocal_id VARCHAR(16),
		clienteLocal_nombre VARCHAR(64),
		comuna VARCHAR(24),
		direccion VARCHAR(32),
		cadena VARCHAR(32),
		categoriaCliente VARCHAR(32),
		subcategoriaCliente VARCHAR(32),
		sucursal VARCHAR(32),/*OficinaVentas*/
		zonaVenta VARCHAR(16),
		supervisor VARCHAR(32),
		preventa VARCHAR(32),
		kam VARCHAR(32),
		centralizado VARCHAR(16),
		agcnc VARCHAR(32),
		año INT,
		mes INT,
		ejecutivo VARCHAR(32),
		diaLlamado VARCHAR(32),
		tipoClub VARCHAR(32),
		categoriaClub VARCHAR(16),
		segmentoClub VARCHAR(32),
		canje VARCHAR(32),
		clienteFugado INT,
		clienteHistorico INT,
		clienteNuevo INT,
		clienteVigente INT,
		clienteRecuperado INT,
		clienteFugaNeto INT,
		clienteCrecimientoNeto INT,
		clienteRecuperadoKg INT,
		clienteFugadoKg INT,
		clienteCrecimientoKg INT,
		brechaFuga_neto INT,
		brechaCrecimiento_neto INT,
		brecha_neto INT,
		brechaFuga_Kg FLOAT,
		brechaCrecimiento_Kg FLOAT,
		brecha_Kg FLOAT,
		venta_clienteFugado INT,
		venta_clienteHistorico INT,
		venta_clientesNuevo INT,
		venta_clientesVigente INT,
		fugados_Kg FLOAT,
		historico_Kg FLOAT,
		nuevos_Kg FLOAT,
		vigentes_Kg FLOAT,
		tipoCall VARCHAR(16),
		kamjr VARCHAR(32),
		jefeVentas VARCHAR(32)
	  );
      
    
    INSERT INTO tabla_fuga_fs
    (clienteLocal_id, clienteLocal_nombre, comuna, direccion, cadena, categoriaCliente, subcategoriaCliente, sucursal,
		zonaVenta, supervisor, preventa, kam, centralizado, agcnc, año, mes, ejecutivo, diaLlamado, tipoClub, 
        categoriaClub, segmentoClub, canje, clienteFugado, clienteHistorico, clienteNuevo, clienteVigente, 
        clienteRecuperado, clienteFugaNeto, clienteCrecimientoNeto, clienteRecuperadoKg, clienteFugadoKg, 
        clienteCrecimientoKg, brechaFuga_neto, brechaCrecimiento_neto, brecha_neto, brechaFuga_Kg, brechaCrecimiento_Kg,
        brecha_Kg, venta_clienteFugado, venta_clienteHistorico, venta_clientesNuevo, venta_clientesVigente, fugados_Kg,
        historico_Kg, nuevos_Kg, vigentes_Kg, tipoCall, kamjr, jefeVentas)
    (    
	SELECT 
		facturaventas.clienteLocal_id,
        clienteLocal.nombre,
		clienteLocal.comuna,
        clienteLocal.direccion,
        "",
        categoriaCliente.nombre,
        subcategoriaCliente.nombre,
        oficinaventas.nombre,
        zonaVentas.nombre,
        "",/* superv */
        "",/* preventas */
        "",/* kam */
        "",/* centralizado */
        facturaventas.agcnc,
        0,/* año */
        0,/* mes */
        "",/* ejecutivo */
        "",/* diaLlamado */
        "",/* tipoClub */
        "",/* catClub */
        "",/* segmClub */
        "",/* canje */
        0,0,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,0,0,0,0,0,
        "",/* tipocall */
        "",/* kamjr */
        "" /* jefeVentas */
	FROM 
		facturaVentas, clienteLocal, categoriaCliente, subcategoriaCliente, zonaVentas, oficinaVentas
	WHERE
		(facturaventas.fecha BETWEEN fechaInicio AND fechaFin)
        AND facturaVentas.clienteLocal_id = clienteLocal.id
		AND clienteLocal.subCategoriaCliente_id = subcategoriacliente.id 
        AND subcategoriacliente.categoriaCliente_id = categoriacliente.id
        AND facturaventas.oficina_id = oficinaVentas.id
        AND oficinaventas.zonaVentas_id = zonaVentas.id
	GROUP BY
		clienteLocal.id
	);		
    
	CREATE TEMPORARY TABLE IF NOT EXISTS tableAuxFuga AS
	(
		SELECT 
			tabla_fuga_fs.clienteLocal_id, 
            tabla_fuga_fs.clienteLocal_nombre, 
            tabla_fuga_fs.comuna, 
            tabla_fuga_fs.direccion, 
            tabla_fuga_fs.cadena, 
            tabla_fuga_fs.categoriaCliente, 
            tabla_fuga_fs.subcategoriaCliente, 
            tabla_fuga_fs.sucursal,
			tabla_fuga_fs.zonaVenta,
            if((SELECT COUNT(facturaventas.id) FROM facturaventas WHERE
            facturaventas.clienteLocal_id = tabla_fuga_fs.clienteLocal_id AND 
            (facturaventas.fecha BETWEEN '2012-01-01' AND fechaInicio) LIMIT 1)!=0,1,0) AS esHistorico
		FROM 
			tabla_fuga_fs, facturaVentas
	);		
    
    
END 
$$

DROP PROCEDURE IF EXISTS sp_reporte_fuga_fs $$

CREATE PROCEDURE sp_reporte_fuga_fs (
  IN fechaInicio VARCHAR(10), 
  IN fechaFin VARCHAR(10)
)
BEGIN
	CALL sp_generate_reporte_fuga_fs(fechaInicio,fechaFin);
    
	/*SELECT * FROM tabla_fuga_fs;*/
	SELECT * FROM tableAuxFuga;
END
$$


/*
CALL sp_reporte_disponibilidad('2018-02-12','2018-02-18');
CALL sp_reporte_arbol_perdidas('2018-02-12','2018-02-18');
CALL sp_reporte_fuga_fs('2018-02-12','2018-02-18');
*/
