/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB;

import Model.CommandNames;
import Model.LocalDB;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.scene.control.Alert;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Patricio
 */
public class PobladorDB_ReporteNS extends PobladorDB
{

    public PobladorDB_ReporteNS(LocalDB db)
    {
        super(db);
    }
    
    public boolean importarPedidos() throws FileNotFoundException, IOException, InvalidFormatException
    {
        int ct_centros=0, ct_oficinas=0, ct_materiales=0, ct_pedidos=0;
        int ctRow=1;
        File archivo=this.openFile(this.dirBase, "pedidos.xlsx");
        if(archivo==null)
        {
          //  CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.INFORMATION, CommandNames.ESTADO_INFO, 
           //         CommandNames.MSG_INFO_FILE_DOESNT_EXISTS);
            return false;
        }
        //archivo existe, comienza procesado...
        try (FileInputStream file = new FileInputStream(archivo)) {
            // leer archivo excel
            XSSFWorkbook worbook = new XSSFWorkbook(file);
            //obtener la hoja que se va leer
            XSSFSheet sheet = worbook.getSheetAt(0);
            
            this.connect();
            while(sheet.getRow(ctRow)!=null)
            {
                System.out.println("Row: "+ctRow);
                
                //RELLENO DE TABLA CENTRO
                String idCentro=sheet.getRow(ctRow).getCell(0).toString().toUpperCase();
                String nombreCentro=sheet.getRow(ctRow).getCell(1).toString().replace("Sucursal ", "");
                System.out.println("Centro: "+idCentro+"/"+nombreCentro);
                if(!this.db.centros.contains(idCentro))
                {
                    this.executeInsert("SELECT id FROM centro WHERE id='"+idCentro+"'",
                            "INSERT INTO centro(id,nombre) VALUES ('"+idCentro+"','"
                                    +nombreCentro+"')");
                    this.db.centros.add(idCentro);
                    ct_centros++;
                }
                
                //RELLENO DE TABLA OFICINA VENTAS
                String idOficina=sheet.getRow(ctRow).getCell(2).toString().toUpperCase();
                String nombreOficina=sheet.getRow(ctRow).getCell(3).toString();
                System.out.println("Of Ventas: "+idOficina+"/"+nombreOficina);
                if(!this.db.oficinas.contains(idOficina))
                {
                    this.executeInsert("SELECT id FROM oficinaVentas WHERE id='"+idOficina+"'",
                            "INSERT INTO oficinaVentas(id,nombre,centro_id) VALUES ('"+idOficina+"','"
                                    +nombreOficina+"','"+idCentro+"')");
                    this.db.oficinas.add(idOficina);
                    ct_oficinas++;
                }
                
                //RELLENO TABLA MATERIAL
                XSSFCell aux=sheet.getRow(ctRow).getCell(5);
                aux.setCellType(CellType.STRING);
                String idMaterial=aux.toString();
                String nombreMaterial=sheet.getRow(ctRow).getCell(6).toString();
                System.out.println("Centro: "+idMaterial+"/"+nombreMaterial);
                if(!this.db.materiales.contains(idMaterial))
                {
                    this.executeInsert("SELECT id FROM material WHERE id='"+idMaterial+"'",
                            "INSERT INTO material(id,nombre) VALUES ('"+idMaterial+"','"
                                    +nombreMaterial+"')");
                    this.db.materiales.add(idMaterial);
                    ct_materiales++;
                }
                
                //RELLENO TABLA PEDIDO     
                aux=sheet.getRow(ctRow).getCell(9);
                aux.setCellType(CellType.STRING);
                String pedidoCj=aux.toString();
                aux=sheet.getRow(ctRow).getCell(10);
                aux.setCellType(CellType.STRING);
                String pedidoKg=aux.toString();
                aux=sheet.getRow(ctRow).getCell(11);
                aux.setCellType(CellType.STRING);
                String pedidoCLP=aux.toString();
                
                String tipoCliente=sheet.getRow(ctRow).getCell(8).toString();
                String fecha=sheet.getRow(ctRow).getCell(7).toString();
                String dia=fecha.substring(0, 2);
                String mes=fecha.substring(3, 5);
                String anio=fecha.substring(6);
                fecha=anio+"-"+mes+"-"+dia;
                
                String idPedido=idMaterial+fecha+idOficina;
                System.out.println("Pedido: "+idPedido);
                if(!this.db.pedidos.contains(idPedido))
                {
                    this.executeInsert("SELECT material_id FROM pedido WHERE material_id='"+idMaterial+"' AND oficina_id='"
                            +idOficina+"' AND fecha='"+fecha+"'",
                            "INSERT INTO pedido(material_id,fecha,oficina_id,tipoCliente,pedidoCj,pedidoKg,pedidoNeto) "
                                + "VALUES ('"+idMaterial+"','"+fecha+"','"+idOficina+"','"+tipoCliente+"','"
                                    +pedidoCj+"','"+pedidoKg+"','"+pedidoCLP+"')");
                    this.db.pedidos.add(idPedido);
                    ct_pedidos++;
                }
                
                System.out.println("-----------------------------");
                ctRow++;
            }
            this.close();
        } catch (Exception e) {
            System.out.println("upsis :c"+e);
        }
        System.out.println("Contadores:");
        System.out.println("Centros: "+ct_centros);
        System.out.println("Oficinas: "+ct_oficinas);
        System.out.println("Materiales: "+ct_materiales);
        System.out.println("pedidos: "+ct_pedidos);
        System.out.println("Total rows: "+ctRow);
        System.out.println("---------------");
        return false;
    }
    
    public boolean importarStock() throws FileNotFoundException, IOException, InvalidFormatException
    {
        int ct_centros=0, ct_materiales=0, ct_stocks=0, ctRow=1;
        File archivo=this.openFile(this.dirBase, "stock.xlsx");
        if(archivo==null)
        {
          //  CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.INFORMATION, CommandNames.ESTADO_INFO, 
            //        CommandNames.MSG_INFO_FILE_DOESNT_EXISTS);
            return false;
        }
        //archivo existe, comienza procesado...
        try (FileInputStream file = new FileInputStream(archivo)) {
            // leer archivo excel
            XSSFWorkbook worbook = new XSSFWorkbook(file);
            //obtener la hoja que se va leer
            XSSFSheet sheet = worbook.getSheetAt(0);
            //obtener todas las filas de la hoja excel

            //extraer datos Centro Distribucion
            
            this.connect();
            while(sheet.getRow(ctRow)!=null)
            {
                System.out.println("Row: "+ctRow);
                
                //RELLENO DE TABLA CENTRO
                String idCentro=sheet.getRow(ctRow).getCell(0).toString().toUpperCase();
                String nombreCentro=sheet.getRow(ctRow).getCell(1).toString().replace("Sucursal ", "");
                System.out.println("Centro: "+idCentro+"/"+nombreCentro);
                if(!validarContenido(idCentro) || !validarContenido(nombreCentro))
                {
                    idCentro="";
                    nombreCentro="";
                    System.out.println("NO AGREGADO!");
                }
                else if(!this.db.centros.contains(idCentro))
                {
                    this.executeInsert("SELECT id FROM centro WHERE id='"+idCentro+"'",
                            "INSERT INTO centro(id,nombre) VALUES ('"+idCentro+"','"
                                    +nombreCentro+"')");
                    this.db.centros.add(idCentro);
                    ct_centros++;
                }
                
                //RELLENO TABLA MATERIAL
                XSSFCell aux=sheet.getRow(ctRow).getCell(3);
                aux.setCellType(CellType.STRING);
                String idMaterial=aux.toString();
                String nombreMaterial=sheet.getRow(ctRow).getCell(4).toString();
                System.out.println("Material: "+idMaterial+"/"+nombreMaterial);
                if(!validarContenido(idMaterial) || !validarContenido(nombreMaterial))
                {
                    idMaterial="";
                    nombreMaterial="";
                    System.out.println("NO AGREGADO!");
                }
                else if(!this.db.materiales.contains(idMaterial))
                {
                    this.executeInsert("SELECT id FROM material WHERE id='"+idMaterial+"'",
                            "INSERT INTO material(id,nombre) VALUES ('"+idMaterial+"','"
                                    +nombreMaterial+"')");
                    this.db.materiales.add(idMaterial);
                    ct_materiales++;
                }
                
                
                //RELLENO TABLA STOCK                
                
                aux=sheet.getRow(ctRow).getCell(7);
                String salidasStock="";
                if(aux!=null)
                {
                    aux.setCellType(CellType.STRING);
                    salidasStock=aux.toString();
                }
                aux=sheet.getRow(ctRow).getCell(8);
                String stock="";
                if(aux!=null)
                {
                    aux.setCellType(CellType.STRING);
                    stock=aux.toString();
                }
                aux=sheet.getRow(ctRow).getCell(9);
                String disponibleStock="";
                if(aux!=null)
                {
                    aux.setCellType(CellType.STRING);
                    disponibleStock=aux.toString();
                }
                
                //OJO, VERIFICAR QUE FECHAS ESTEN INGRESADAS CORRECTAMENTE
                String fecha=sheet.getRow(ctRow).getCell(5).toString();
                String dia=fecha.substring(0, 2);
                String mes=fecha.substring(3, 5);
                String anio=fecha.substring(6);
                fecha=anio+"-"+mes+"-"+dia;
                
                String idStock=idMaterial+fecha+idCentro;
                System.out.println("Stock: "+idStock);
                if(!validarContenido(idMaterial) || !validarContenido(fecha) || !validarContenido(idCentro))
                {
                    idMaterial="";
                    fecha="";
                    idCentro="";
                    System.out.println("NO AGREGADO!");
                }
                else if(!this.db.stocks.contains(idStock))
                {
                    this.executeInsert("SELECT material_id FROM stock WHERE material_id='"+idMaterial+"' AND centro_id='"
                            +idCentro+"' AND fecha='"+fecha+"'",
                            "INSERT INTO stock(material_id,fecha,centro_id,salidas,stock,disponible) "
                                + "VALUES ("+((validarContenido(idMaterial))? "'"+idMaterial+"'" : "null")+","
                                    +((validarContenido(fecha))? "'"+fecha+"'" : "null")+","
                                    +((validarContenido(idCentro))? "'"+idCentro+"'" : "null")+","
                                    +((validarContenido(salidasStock))? "'"+salidasStock+"'" : "null")+","
                                    +((validarContenido(stock))? "'"+stock+"'" : "null")+","
                                    +((validarContenido(disponibleStock))? "'"+disponibleStock+"'" : "null")+")");
                    this.db.stocks.add(idStock);
                    ct_stocks++;
                }
                System.out.println("-----------------------------");
                ctRow++;
            }
            this.close();
        } catch (Exception e) {
            System.out.println("upsis :c"+e);
        }
        System.out.println("Contadores:");
        System.out.println("Centros: "+ct_centros);
        System.out.println("Materiales: "+ct_materiales);
        System.out.println("Stocks: "+ct_stocks);
        System.out.println("Total rows: "+ctRow);
        System.out.println("---------------");
        return false;
    }
}
