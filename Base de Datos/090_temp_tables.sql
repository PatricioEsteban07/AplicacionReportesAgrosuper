
DROP TEMPORARY TABLE IF EXISTS pedidoBase;
DROP TEMPORARY TABLE IF EXISTS despachoBase;
DROP TEMPORARY TABLE IF EXISTS stockBase;
DROP TEMPORARY TABLE IF EXISTS tableBase;
DROP TEMPORARY TABLE IF EXISTS tableBase2;
DROP TEMPORARY TABLE IF EXISTS tb;
DROP TEMPORARY TABLE IF EXISTS tablaDisponibilidad;




CREATE TEMPORARY TABLE IF NOT EXISTS tableBase AS
(
SELECT pedido.centro_id AS centro_id, material.sector_id AS sector_id, material.agrupado_id AS agrupado_id, 
pedido.fechaEntrega AS fecha, SUM(pedido_material.cantidadCj) AS pedido_Cj,
SUM(pedido_material.pesoKg) AS pedido_Kg, SUM(pedido_material.precioNeto) AS pedido_neto,
0 AS disponible_Cj, 0 AS disponible_Kg, 0 AS despacho_Cj, 0 AS despacho_Kg
	FROM pedido, pedido_material, material
	WHERE pedido.fechaEntrega = '2018-01-30' AND pedido.id = pedido_material.pedido_id 
    AND pedido_material.material_id = material.id
    GROUP BY material.agrupado_id, pedido.fechaEntrega, pedido.centro_id
);

INSERT INTO tableBase (centro_id,sector_id,agrupado_id,fecha,pedido_Cj,pedido_Kg,pedido_neto,disponible_Cj,
disponible_Kg,despacho_Cj,despacho_Kg)
(
SELECT despacho.centro_id, material.sector_id, material.agrupado_id,
despacho.fecha, 0, 0, 0, 0, 0, TRUNCATE(SUM(despacho_material.despachoCj),1), 
TRUNCATE(SUM(despacho_material.despachoKg),3)
FROM despacho, despacho_material, material
WHERE despacho.fecha = '2018-01-30' AND despacho.id = despacho_material.despacho_id
    AND despacho_material.material_id = material.id
GROUP BY material.agrupado_id, despacho.fecha, despacho.centro_id
);

INSERT INTO tableBase (centro_id,sector_id,agrupado_id,fecha,pedido_Cj,pedido_Kg,pedido_neto,disponible_Cj,
disponible_Kg,despacho_Cj,despacho_Kg)
(
SELECT stock.centro_id, material.sector_id, material.agrupado_id, stock.fecha, 0, 0, 0, 
TRUNCATE(SUM((stock.stock+stock.salidas)/material.pesoCaja),1), TRUNCATE(SUM(stock.stock+stock.salidas), 3), 0, 0
FROM stock, material
WHERE stock.fecha = '2018-01-30' AND stock.material_id = material.id
GROUP BY material.agrupado_id, stock.fecha, stock.centro_id
);

CREATE TEMPORARY TABLE IF NOT EXISTS tb AS
(
SELECT centro_id, centro.nombre AS centro_nombre, sector_id, sector.nombre AS sector_nombre, agrupado_id,
agrupado.nombre AS agrupado_nombre, fecha, SUM(pedido_Cj) AS pedido_Cj, SUM(pedido_Kg) AS pedido_Kg, 
SUM(pedido_neto) AS pedido_neto, SUM(disponible_Cj) AS disponible_Cj, SUM(disponible_Kg) AS disponible_Kg, 
SUM(despacho_Cj) AS despacho_Cj, SUM(despacho_Kg) AS despacho_Kg
FROM tableBase, agrupado, centro, sector
WHERE tableBase.centro_id = centro.id AND tableBase.sector_id = sector.id AND tableBase.agrupado_id = agrupado.id
GROUP BY agrupado_id, centro_id, fecha
);

CREATE TEMPORARY TABLE IF NOT EXISTS tablaDisponibilidad AS
(
SELECT tb.centro_id, replace(tb.centro_nombre,"Sucursal ","") AS centro_nombre, tb.sector_id, tb.sector_nombre, tb.agrupado_id, tb.agrupado_nombre,
tb.fecha, TRUNCATE(tb.pedido_Cj,1) AS pedido_Cj, TRUNCATE(tb.despacho_Cj,1) AS despacho_Cj, 
TRUNCATE(tb.disponible_Cj,1) AS disponible_Cj, TRUNCATE(tb.pedido_Kg,3) AS pedido_Kg, 
TRUNCATE(tb.pedido_neto,0) AS pedido_neto, TRUNCATE(tb.disponible_Kg,3) AS disponible_Kg, 
TRUNCATE(IF(tb.disponible_Cj<tb.pedido_Cj,tb.pedido_Cj-tb.disponible_Cj,0),1) AS faltante_Cj,
TRUNCATE(IF(tb.disponible_Kg<tb.pedido_Kg,tb.pedido_Kg-tb.disponible_Kg,0),3) AS faltante_Kg,
WEEK(tb.fecha) AS semana,
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
FROM( tb )
);

SELECT * FROM tablaDisponibilidad;

SELECT SUM(pedido_Cj), SUM(pedido_Kg), SUM(pedido_neto), SUM(disponible_Kg), SUM(disponible_cj) FROM tablaDisponibilidad;

SELECT TRUNCATE(SUM(stock.stock+stock.salidas)/material.pesoCaja,1) AS disponible_Cj,
TRUNCATE(SUM(stock.stock+stock.salidas), 3) AS disponible_Kg
FROM stock, material
WHERE stock.fecha = '2018-01-30' AND stock.material_id = material.id
