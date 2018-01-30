
SELECT Temp.oficina_id AS centro_id, oficinaventas.nombre AS centro_nombre, 
material.sector_id AS sector_id, material.agrupado_id AS agrupado_id, agrupado.nombre AS agrupado_nombre,
Temp.fecha AS pedido_fecha, Temp.pedidoCj AS pedido_cj, Temp.pedidoKg AS pedido_kg,
Temp.pedidoNeto AS pedido_neto, (stock.disponible/material.pesoCaja) AS disponibilidad_cj,
stock.disponible AS disponibilidad_kg, despacho.despachoCj AS despacho_cj, despacho.despachoKg AS despacho_kg
 FROM ((SELECT * FROM pedido WHERE pedido.fecha BETWEEN '2017-11-07' AND '2017-11-10') AS Temp)
LEFT JOIN oficinaventas ON (Temp.oficina_id = oficinaventas.id)
LEFT JOIN material ON (material.id = Temp.material_id)
LEFT JOIN agrupado ON (agrupado.id = material.agrupado_id)
LEFT JOIN stock ON (Temp.fecha = stock.fecha)
LEFT JOIN despacho ON (Temp.fecha = despacho.fecha)
