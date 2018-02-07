
DROP TEMPORARY TABLE IF  EXISTS tableBase;

CREATE TEMPORARY TABLE IF NOT EXISTS pedidoBase AS
(
SELECT centro.id AS centro_id, material.sector_id,
pedido.id AS pedido_id, pedido.fechaEntrega AS pedido_fechaEntrega, material.id AS material_id, 
material.agrupado_id AS agrupado_id, pedido_material.cantidadCj AS pedido_Cj, pedido_material.pesoKg AS pedido_kg, 
pedido_material.precioNeto AS pedido_neto, material.pesoCaja AS material_pesoCaja
	FROM pedido, pedido_material, material, centro
	WHERE pedido.fechaEntrega = '2018-01-30' AND pedido.id = pedido_material.pedido_id 
    AND pedido_material.material_id = material.id AND pedido.centro_id = centro.id
);

CREATE TEMPORARY TABLE IF NOT EXISTS despachoBase AS
(
SELECT centro.id AS centro_id, material.sector_id,
despacho.id AS despacho_id, despacho.fecha AS despacho_fecha, material.id AS material_id,
material.agrupado_id AS agrupado_id, despacho_material.despachoCj AS despacho_Cj, despacho_material.despachoKg despacho_kg
	FROM despacho, despacho_material, material, centro
	WHERE despacho.fecha = '2018-01-30' AND despacho.id = despacho_material.despacho_id
    AND despacho_material.material_id = material.id AND despacho.centro_id = centro.id
);

CREATE TEMPORARY TABLE IF NOT EXISTS stockBase AS
(
SELECT centro.id AS centro_id, material.sector_id, stock.fecha AS stock_fecha, material.id AS material_id, 
material.agrupado_id AS agrupado_id, TRUNCATE((stock.stock-stock.salidas), 3) AS stock_Kg, 
(TRUNCATE((stock.stock-stock.salidas)/material.pesoCaja,1)) AS stock_Cj
	FROM stock, material, centro
	WHERE stock.fecha = '2018-01-30' AND stock.material_id = material.id AND stock.centro_id = centro.id
);

SELECT SUM(tableBase.pedido_Cj), SUM(tableBase.pedido_kg), SUM(tableBase.pedido_neto) FROM tableBase;

CREATE TEMPORARY TABLE IF NOT EXISTS tableBase2 AS
(
SELECT Temp.centro_id AS centro_id, centro.nombre AS centro_nombre, 
material.sector_id AS sector_id, material.agrupado_id AS agrupado_id, agrupado.nombre AS agrupado_nombre,
Temp.fechaEntrega AS pedido_fecha, Temp.pedidoCj AS pedido_cj, Temp.pedidoKg AS pedido_kg,
Temp.pedidoNeto AS pedido_neto, 0 AS disponibilidad_cj, 0 AS disponibilidad_kg, 0 AS despacho_cj, 
0 AS despacho_kg

FROM( tableBase )

);
----------------------------------------------------------

CREATE TEMPORARY TABLE IF NOT EXISTS tablePedido AS
(
SELECT Temp.centro_id AS centro_id, centro.nombre AS centro_nombre, 
material.sector_id AS sector_id, material.agrupado_id AS agrupado_id, agrupado.nombre AS agrupado_nombre,
Temp.material_id AS material_id, material.nombre AS material_nombre,
Temp.fechaEntrega AS pedido_fecha, Temp.pedidoCj AS pedido_cj, Temp.pedidoKg AS pedido_kg,
Temp.pedidoNeto AS pedido_neto, material.pesoCaja AS disponibilidad_cj, 0 AS disponibilidad_kg, 0 AS despacho_cj, 
0 AS despacho_kg
 FROM ((SELECT * FROM pedido WHERE pedido.fechaEntrega BETWEEN '2018-01-01' AND '2018-01-30') AS Temp)
LEFT JOIN centro ON (Temp.centro_id = centro.id)
LEFT JOIN material ON (material.id = Temp.material_id)
LEFT JOIN agrupado ON (agrupado.id = material.agrupado_id)
);

SELECT * FROM tablePedido;

SELECT tablePedido.centro_id, tablePedido.centro_nombre, tablePedido.sector_id, tablePedido.agrupado_id, 
tablePedido.agrupado_nombre, tablePedido.pedido_fecha, tablePedido.pedido_cj, tablePedido.pedido_kg, 
tablePedido.pedido_neto, (stock.disponible/tablePedido.disponibilidad_cj) AS disponibilidad_cj, stock.disponible AS
disponibilidad_kg, despacho.despachoCj AS despacho_cj, despacho.despachoKg AS despacho_kg
FROM tablePedido
LEFT JOIN stock ON (tablePedido.material_id = stock.material_id AND tablePedido.pedido_fecha = stock.fecha
	AND tablePedido.centro_id = stock.centro_id)
LEFT JOIN despacho ON (tablePedido.material_id = despacho.material_id 
AND tablePedido.pedido_fecha = despacho.fecha AND tablePedido.centro_id = despacho.centro_id);

------------------------------

CREATE TEMPORARY TABLE IF NOT EXISTS tablePedido AS 
(SELECT pedido.oficina_id AS centro_id, null AS centro_nombre, 
null AS sector_id, null AS agrupado_id, null AS agrupado_nombre,
Temp.fecha AS pedido_fecha, Temp.pedidoCj AS pedido_cj, Temp.pedidoKg AS pedido_kg,
Temp.pedidoNeto AS pedido_neto, (stock.disponible/material.pesoCaja) AS disponibilidad_cj,
stock.disponible AS disponibilidad_kg, despacho.despachoCj AS despacho_cj, despacho.despachoKg AS despacho_kg 
FROM pedido)

select week('2018-01-01');

