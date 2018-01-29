/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB;

import Model.LocalDB;
import com.monitorjbl.xlsx.StreamingReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
        int ct_centros=0, ct_oficinas=0, ct_materiales=0, ct_pedidos=0, ctRow=0;
        File archivo=this.openFile(this.dirBase, "pedidos.xlsx");
        if(archivo==null)
        {
          //  CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.INFORMATION, CommandNames.ESTADO_INFO, 
           //         CommandNames.MSG_INFO_FILE_DOESNT_EXISTS);
            return false;
        }
        //archivo existe, comienza procesado...
        try (InputStream is = new FileInputStream(archivo)) {
            
            // leer archivo excel
            Workbook workbook = StreamingReader.builder()
                    .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                    .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                    .open(is); 
            //obtener la hoja que se va leer
            Sheet sheet = workbook.getSheetAt(0);
            //obtener todas las filas de la hoja excel
            
            this.connect();
            for(Row row : sheet)
            {
                System.out.println("Row: "+ctRow);
                if(ctRow!=0)
                {
                    //RELLENO DE TABLA CENTRO
                    String idCentro=row.getCell(0).getStringCellValue().toUpperCase();
                    String nombreCentro=row.getCell(1).getStringCellValue().replace("Sucursal ", "");
                  //  System.out.println("Centro: "+idCentro+"/"+nombreCentro);
                    if(!validarContenido(idCentro) || !validarContenido(nombreCentro))
                    {
                        System.out.println("Row: "+ctRow+" - Centro:"+idCentro+"/"+nombreCentro);
                        idCentro="";
                        nombreCentro="";
                    }
                    else if(!this.db.centros.contains(idCentro))
                    {
                        this.executeInsert("SELECT id FROM centro WHERE id='"+idCentro+"'",
                                "INSERT INTO centro(id,nombre) VALUES ('"+idCentro+"','"
                                        +nombreCentro+"')");
                        this.db.centros.add(idCentro);
                        ct_centros++;
                    }

                    //RELLENO DE TABLA OFICINA VENTAS
                    String idOficina=row.getCell(2).getStringCellValue().toUpperCase();
                    String nombreOficina=row.getCell(3).getStringCellValue();
                 //   System.out.println("Of Ventas: "+idOficina+"/"+nombreOficina);
                    if(!validarContenido(idOficina) || !validarContenido(nombreOficina))
                    {
                        System.out.println("Row: "+ctRow+" - Oficina:"+idOficina+"/"+nombreOficina);
                        idOficina="";
                        nombreOficina="";
                    }
                    else if(!this.db.oficinas.contains(idOficina))
                    {
                        this.executeInsert("SELECT id FROM oficinaVentas WHERE id='"+idOficina+"'",
                                "INSERT INTO oficinaVentas(id,nombre,centro_id) VALUES ('"+idOficina+"','"
                                        +nombreOficina+"','"+idCentro+"')");
                        this.db.oficinas.add(idOficina);
                        ct_oficinas++;
                    }

                    //RELLENO TABLA MATERIAL
                    String idMaterial=row.getCell(5).getStringCellValue();
                    String nombreMaterial=row.getCell(6).getStringCellValue();
                 //   System.out.println("Centro: "+idMaterial+"/"+nombreMaterial);
                    if(!validarContenido(idMaterial) || !validarContenido(nombreMaterial))
                    {
                        System.out.println("Row: "+ctRow+" - Material:"+idMaterial+"/"+nombreMaterial);
                        idCentro="";
                        nombreCentro="";
                    }
                    else if(!this.db.materiales.contains(idMaterial))
                    {
                        this.executeInsert("SELECT id FROM material WHERE id='"+idMaterial+"'",
                                "INSERT INTO material(id,nombre) VALUES ('"+idMaterial+"','"
                                        +nombreMaterial+"')");
                        this.db.materiales.add(idMaterial);
                        ct_materiales++;
                    }

                    //RELLENO TABLA PEDIDO     
                    String pedidoCj=row.getCell(9).getStringCellValue();
                    String pedidoKg=row.getCell(10).getStringCellValue();
                    String pedidoCLP=row.getCell(11).getStringCellValue();

                    String tipoCliente=row.getCell(8).getStringCellValue();
                    String fecha=row.getCell(7).getStringCellValue();
                    String dia=fecha.substring(0, 2);
                    String mes=fecha.substring(3, 5);
                    String anio=fecha.substring(6);
                    fecha=anio+"-"+mes+"-"+dia;

                    String idPedido=idMaterial+fecha+idOficina;
                //    System.out.println("Pedido: "+idPedido);
                    if(!validarContenido(idMaterial) || !validarContenido(fecha) || !validarContenido(idOficina))
                    {
                        System.out.println("Row: "+ctRow+" - Pedido:"+idCentro+"/"+nombreCentro+"/"+idOficina);
                    }
                    else if(!this.db.pedidos.contains(idPedido))
                    {
                        this.executeInsert("SELECT material_id FROM pedido WHERE material_id='"+idMaterial+"' AND oficina_id='"
                                +idOficina+"' AND fecha='"+fecha+"'",
                                "INSERT INTO pedido(material_id,fecha,oficina_id,tipoCliente,pedidoCj,pedidoKg,pedidoNeto) "
                                    + "VALUES ('"+idMaterial+"','"+fecha+"','"+idOficina+"','"+tipoCliente+"','"
                                        +pedidoCj+"','"+pedidoKg+"','"+pedidoCLP+"')");
                        this.db.pedidos.add(idPedido);
                        ct_pedidos++;
                    }
                //    System.out.println("-----------------------------");
                    ctRow++;
                }
            }
            this.close();
        } catch (Exception e) {
            System.out.println("upsis :c "+e);
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
        int ct_centros=0, ct_materiales=0, ct_stocks=0, ctRow=0;
        File archivo=this.openFile(this.dirBase, "stock.xlsx");
        if(archivo==null)
        {
          //  CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.INFORMATION, CommandNames.ESTADO_INFO, 
            //        CommandNames.MSG_INFO_FILE_DOESNT_EXISTS);
            return false;
        }
        //archivo existe, comienza procesado...
        try (InputStream is = new FileInputStream(archivo)) {
            
            // leer archivo excel
            Workbook workbook = StreamingReader.builder()
                    .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                    .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                    .open(is); 
            //obtener la hoja que se va leer
            Sheet sheet = workbook.getSheetAt(0);
            //obtener todas las filas de la hoja excel
            
            this.connect();
            for(Row row : sheet)
            {
                System.out.println("Row: "+ctRow);
                if(ctRow!=0)
                {
                    //RELLENO DE TABLA CENTRO
                    String idCentro=row.getCell(0).getStringCellValue().toUpperCase();
                    String nombreCentro=row.getCell(1).getStringCellValue().replace("Sucursal ", "");
                    System.out.println("Centro: "+idCentro+"/"+nombreCentro);
                    if(!validarContenido(idCentro) || !validarContenido(nombreCentro))
                    {
                        System.out.println("Row: "+ctRow+" - Centro:"+idCentro+"/"+nombreCentro);
                        idCentro="";
                        nombreCentro="";
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
                    String idMaterial=row.getCell(3).getStringCellValue();
                    String nombreMaterial=row.getCell(4).getStringCellValue();
                    System.out.println("Material: "+idMaterial+"/"+nombreMaterial);
                    if(!validarContenido(idMaterial) || !validarContenido(nombreMaterial))
                    {
                        System.out.println("Row: "+ctRow+" - Material:"+idMaterial+"/"+nombreMaterial);
                        idMaterial="";
                        nombreMaterial="";
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

                    String salidasStock=row.getCell(7).getStringCellValue();
                    String stock=row.getCell(8).getStringCellValue();
                    String disponibleStock=row.getCell(9).getStringCellValue();

                    //OJO, VERIFICAR QUE FECHAS ESTEN INGRESADAS CORRECTAMENTE
                    String fecha=row.getCell(5).getStringCellValue();
                    String dia=fecha.substring(0, 2);
                    String mes=fecha.substring(3, 5);
                    String anio=fecha.substring(6);
                    fecha=anio+"-"+mes+"-"+dia;

                    String idStock=idMaterial+fecha+idCentro;
                    System.out.println("Stock: "+idStock);
                    if(!validarContenido(idMaterial) || !validarContenido(fecha) || !validarContenido(idCentro))
                    {
                        System.out.println("Row: "+ctRow+" - Stock:"+idMaterial+"/"+fecha+"/"+idCentro);
                        idMaterial="";
                        fecha="";
                        idCentro="";
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
            }
            this.close();
        } catch (Exception e) {
            System.out.println("upsis :c "+e);
        }
        System.out.println("Contadores:");
        System.out.println("Centros: "+ct_centros);
        System.out.println("Materiales: "+ct_materiales);
        System.out.println("Stocks: "+ct_stocks);
        System.out.println("Total rows: "+ctRow);
        System.out.println("---------------");
        return false;
    }
    
    public boolean importarDespachos() throws FileNotFoundException, IOException, InvalidFormatException
    {
        int ct_centros=0, ct_materiales=0, ct_clientes=0, ct_despachos=0, ctRow=0;
        File archivo=this.openFile(this.dirBase, "despacho-faltantes.xlsx");
        if(archivo==null)
        {
          //  CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.INFORMATION, CommandNames.ESTADO_INFO, 
            //        CommandNames.MSG_INFO_FILE_DOESNT_EXISTS);
            return false;
        }
        //archivo existe, comienza procesado...
        try (InputStream is = new FileInputStream(archivo)) {
            
            // leer archivo excel
            Workbook workbook = StreamingReader.builder()
                    .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                    .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                    .open(is); 
            
            //obtener la hoja que se va leer
            Sheet sheet = workbook.getSheetAt(0);
            //obtener todas las filas de la hoja excel

            //extraer datos Centro Distribucion
            this.connect();
            for(Row row : sheet)
            {
                System.out.println("Row: "+ctRow);
                if(ctRow!=0)
                {
                    //RELLENO DE TABLA CENTRO
                    String idCentro=row.getCell(4).getStringCellValue().toUpperCase();
                    String nombreCentro=row.getCell(5).getStringCellValue().replace("Sucursal ", "");
                    System.out.println("Centro: "+idCentro+"/"+nombreCentro);
                    if(!validarContenido(idCentro) || !validarContenido(nombreCentro))
                    {
                        System.out.println("Row: "+ctRow+" - Centro:"+idCentro+"/"+nombreCentro);
                        idCentro="";
                        nombreCentro="";
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
                    String idMaterial=row.getCell(12).getStringCellValue();
                    String nombreMaterial=row.getCell(13).getStringCellValue();
                    System.out.println("Material: "+idMaterial+"/"+nombreMaterial);
                    if(!validarContenido(idMaterial) || !validarContenido(nombreMaterial))
                    {
                        System.out.println("Row: "+ctRow+" - Material:"+idMaterial+"/"+nombreMaterial);
                        idMaterial="";
                        nombreMaterial="";
                    }
                    else if(!this.db.materiales.contains(idMaterial))
                    {
                        this.executeInsert("SELECT id FROM material WHERE id='"+idMaterial+"'",
                                "INSERT INTO material(id,nombre) VALUES ('"+idMaterial+"','"
                                        +nombreMaterial+"')");
                        this.db.materiales.add(idMaterial);
                        ct_materiales++;
                    }
                    System.out.println("pase");
                    //RELLENO TABLA CLIENTES
                    String idCliente=row.getCell(7).getStringCellValue();
                    String nombreCliente=row.getCell(8).getStringCellValue().replace("'", "");
                    String tipoCliente=row.getCell(3).getStringCellValue();
                    System.out.println("Cliente: "+idCliente+"/"+nombreCliente);
                    if(!validarContenido(idCliente) || !validarContenido(nombreCliente))
                    {
                        System.out.println("Row: "+ctRow+" - Cliente:"+idCliente+"/"+nombreCliente);
                        idCliente="";
                        nombreCliente="";
                    }
                    else if(!this.db.clientes.contains(idCliente))
                    {
                        this.executeInsert("SELECT id FROM cliente WHERE id='"+idCliente+"'",
                                "INSERT INTO cliente(id,nombre,tipoCliente) VALUES ('"+idCliente+"','"
                                        +nombreCliente+"',"
                                        +((validarContenido(tipoCliente))? "'"+tipoCliente+"'" : "null")+")");
                        this.db.clientes.add(idCliente);
                        ct_clientes++;
                    }


                    //RELLENO TABLA DESPACHOS                

                    String despachoKg="";
                        despachoKg=row.getCell(14).getStringCellValue();
                    String faltanteKg="";
                        faltanteKg=row.getCell(15).getStringCellValue();
                    String despachoCj="";
                        despachoCj=row.getCell(16).getStringCellValue();
                    String faltanteCj="";
                        faltanteCj=row.getCell(17).getStringCellValue();

                    //OJO, VERIFICAR QUE FECHAS ESTEN INGRESADAS CORRECTAMENTE
                    String fecha=row.getCell(6).getStringCellValue();
                    String dia=fecha.substring(0, 2);
                    String mes=fecha.substring(3, 5);
                    String anio=fecha.substring(6);
                    fecha=anio+"-"+mes+"-"+dia;

                    String idDespacho=idMaterial+fecha+idCentro+idCliente;
                    System.out.println("Despacho: "+idDespacho);
                    if(!validarContenido(idMaterial) || !validarContenido(fecha) || !validarContenido(idCentro)
                            || !validarContenido(idCliente))
                    {
                        System.out.println("Row: "+ctRow+" - Despacho:"+idMaterial+"/"+fecha+"/"+idCentro+"/"+idCliente);
                        idMaterial="";
                        fecha="";
                        idCentro="";
                        idCliente="";
                    }
                    else if(!this.db.despachos.contains(idDespacho))
                    {
                        this.executeInsert("SELECT material_id FROM despacho WHERE material_id='"+idMaterial+"' AND centro_id='"
                                +idCentro+"' AND fecha='"+fecha+"' AND cliente_id='"+idCliente+"'",
                                "INSERT INTO despacho(material_id,fecha,centro_id,cliente_id,despachoKg,faltanteKg,despachoCj,"
                                        + "faltanteCj) VALUES ("+((validarContenido(idMaterial))? "'"+idMaterial+"'" : "null")+","
                                        +((validarContenido(fecha))? "'"+fecha+"'" : "null")+","
                                        +((validarContenido(idCentro))? "'"+idCentro+"'" : "null")+","
                                        +((validarContenido(idCliente))? "'"+idCliente+"'" : "null")+","
                                        +((validarContenido(despachoKg))? "'"+despachoKg+"'" : "null")+","
                                        +((validarContenido(faltanteKg))? "'"+faltanteKg+"'" : "null")+","
                                        +((validarContenido(despachoCj))? "'"+despachoCj+"'" : "null")+","
                                        +((validarContenido(faltanteCj))? "'"+faltanteCj+"'" : "null")+")");
                        this.db.despachos.add(idDespacho);
                        ct_despachos++;
                    }

                    System.out.println("-----------------------------");
                }
                ctRow++;
            }
            this.close();
        } catch (Exception e) {
            System.out.println("upsis :c"+e);
        }
        System.out.println("Contadores:");
        System.out.println("Centros: "+ct_centros);
        System.out.println("Materiales: "+ct_materiales);
        System.out.println("Clientes: "+ct_clientes);
        System.out.println("Despachos: "+ct_despachos);
        System.out.println("Total rows: "+ctRow);
        System.out.println("---------------");
        return false;
    }
}
