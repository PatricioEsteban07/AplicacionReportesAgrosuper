/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB.Threads;

import Model.LocalDB;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Patricio
 */
public class ThreadProcess_Pedidos extends ThreadProcess
{
    public ThreadProcess_Pedidos(int id, LocalDB db, Buzon buzon)
    {
        super(id,db,buzon);
    }

    @Override
    public void run()
    {
        while(this.flagExterno || this.flagPropio)
        {
            Row row=this.buzon.obtenerJob();
            if(row==null && !this.flagExterno)
            {
                this.flagPropio=false;
            }
            //obtengo una row
            if(row!=null)
            {
                PaqueteBuzon paqueteAux=new PaqueteBuzon();
                //RELLENO DE TABLA CENTRO
                String idCentro=(row.getCell(0)==null) ? "" : row.getCell(0).getStringCellValue().toUpperCase();
                String nombreCentro=(row.getCell(1)==null) ? "" : row.getCell(1).getStringCellValue().replace("Sucursal ", "");
              //  System.out.println("Centro: "+idCentro+"/"+nombreCentro);
                if(!validarContenido(idCentro) || !validarContenido(nombreCentro))
                {
                    idCentro="";
                    nombreCentro="";
                }
                else if(!this.db.centros.contains(idCentro))
                {
                    this.db.centros.add(idCentro);
                    paqueteAux.addContenido("Centro",idCentro,"INSERT INTO centro(id,nombre) VALUES ('"
                            +idCentro+"','"+nombreCentro+"')");
                }

                //RELLENO DE TABLA OFICINA VENTAS
                String idOficina=(row.getCell(2)==null) ? "" : row.getCell(2).getStringCellValue().toUpperCase();
                String nombreOficina=(row.getCell(3)==null) ? "" : row.getCell(3).getStringCellValue();

                if(!validarContenido(idOficina) || !validarContenido(nombreOficina))
                {
                    idOficina="";
                    nombreOficina="";
                }
                else if(!this.db.oficinas.contains(idOficina))
                {
                    this.db.oficinas.add(idOficina);
                    paqueteAux.addContenido("Oficina",idOficina, "INSERT INTO oficinaVentas(id,nombre,centro_id) "
                            + "VALUES ('"+idOficina+"','"+nombreOficina+"','"+idCentro+"')");
                }

                //RELLENO TABLA MATERIAL
                String idMaterial=(row.getCell(5)==null) ? "" : row.getCell(5).getStringCellValue();
                String nombreMaterial=(row.getCell(6)==null) ? "" : row.getCell(6).getStringCellValue();
             //   System.out.println("Centro: "+idMaterial+"/"+nombreMaterial);
                if(!validarContenido(idMaterial) || !validarContenido(nombreMaterial))
                {
                    idCentro="";
                    nombreCentro="";
                }
                else if(!this.db.materiales.contains(idMaterial))
                {
                    this.db.materiales.add(idMaterial);
                    paqueteAux.addContenido("Material",idMaterial,"INSERT INTO material(id,nombre) VALUES ('"
                            +idMaterial+"','"+nombreMaterial+"')");
                }

                //RELLENO TABLA PEDIDO     
                String pedidoCj=(row.getCell(9)==null) ? "" : row.getCell(9).getStringCellValue().replace(".", "");
                String pedidoKg=(row.getCell(10)==null) ? "" : row.getCell(10).getStringCellValue().replace(".", "").replace(",", ".");
                String pedidoCLP=(row.getCell(11)==null) ? "" : row.getCell(11).getStringCellValue().replace(".", "");

                String tipoCliente=(row.getCell(8)==null) ? "" : row.getCell(8).getStringCellValue();
                String fecha=(row.getCell(7)==null) ? "" : row.getCell(7).getStringCellValue();
                if(validarContenido(fecha))
                {
                    String dia=fecha.substring(0, 2);
                    String mes=fecha.substring(3, 5);
                    String anio=fecha.substring(6);
                    fecha=anio+"-"+mes+"-"+dia;    
                }

                String idPedido=idMaterial+fecha+idOficina;
                if(!validarContenido(idMaterial) || !validarContenido(fecha) || !validarContenido(idOficina))
                {
                    idCentro="";
                }
                else if(!this.db.pedidos.contains(idPedido))
                {
                    this.db.pedidos.add(idPedido);
                    paqueteAux.addContenido("Pedido",idPedido,"INSERT INTO pedido(id,material_id,fecha,oficina_id,"
                            + "tipoCliente,pedidoCj,pedidoKg,pedidoNeto) "+ "VALUES ('"+idPedido+"','"+idMaterial
                            +"','"+fecha+"','"+idOficina+"','"+tipoCliente+"','"+pedidoCj+"','"+pedidoKg+"','"
                            +pedidoCLP+"')");
                }
                this.buzon.enviarPaquete("Centro:"+row.getRowNum(),paqueteAux);
            }
        }
    }
    
}
