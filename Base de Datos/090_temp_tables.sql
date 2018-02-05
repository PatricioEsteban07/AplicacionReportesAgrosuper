SELECT pedido.id, pedido.fechaEntrega, pedido_material.material_id, pedido_material.cantidadCj, pedido_material.pesoKg, pedido_material.precioNeto
FROM pedido, pedido_material
WHERE pedido.fechaEntrega = '2018-01-20' AND pedido.id = pedido_material.pedido_id;
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

DROP TEMPORARY TABLE IF  EXISTS tablePedido;
------------------------------

CREATE TEMPORARY TABLE IF NOT EXISTS tablePedido AS 
(SELECT pedido.oficina_id AS centro_id, null AS centro_nombre, 
null AS sector_id, null AS agrupado_id, null AS agrupado_nombre,
Temp.fecha AS pedido_fecha, Temp.pedidoCj AS pedido_cj, Temp.pedidoKg AS pedido_kg,
Temp.pedidoNeto AS pedido_neto, (stock.disponible/material.pesoCaja) AS disponibilidad_cj,
stock.disponible AS disponibilidad_kg, despacho.despachoCj AS despacho_cj, despacho.despachoKg AS despacho_kg 
FROM pedido)

select week('2018-01-01');

