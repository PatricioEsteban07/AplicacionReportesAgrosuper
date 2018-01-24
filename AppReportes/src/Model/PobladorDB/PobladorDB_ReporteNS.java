/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB;

import Model.CommandNames;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
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

    public PobladorDB_ReporteNS()
    {
        super();
    }
    
    public boolean importarPedidos() throws FileNotFoundException, IOException, InvalidFormatException
    {
        File archivo=this.openFile(this.dirBase, "ejemplo2.xlsx");
        if(archivo==null)
        {
            CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.INFORMATION, CommandNames.ESTADO_INFO, 
                    CommandNames.MSG_INFO_FILE_DOESNT_EXISTS);
            return false;
        }
        //archivo existe, comienza procesado...

        try (FileInputStream file = new FileInputStream(archivo)) {
            // leer archivo excel
            XSSFWorkbook worbook = new XSSFWorkbook(file);
            //obtener la hoja que se va leer
            XSSFSheet sheet = worbook.getSheetAt(0);
            //obtener todas las filas de la hoja excel
          //  Iterator<Row> rowIterator = sheet.iterator();

            //extraer datos Centro Distribucion
            Set<String> centros = new HashSet<String>();
            Set<String> oficinas = new HashSet<String>();
            Set<String> materiales = new HashSet<String>();
            
            System.out.println("pase");
            
            this.connect();
            System.out.println("pase");
            int ctRow=1;
            while(sheet.getRow(ctRow)!=null)
            {
                System.out.println("Row: "+ctRow);

                
                
                //RELLENO DE TABLA CENTRO
                String idCentro=sheet.getRow(ctRow).getCell(0).toString().toUpperCase();
                String nombreCentro=sheet.getRow(ctRow).getCell(1).toString().replace("Sucursal ", "");
                System.out.println("Centro: "+idCentro+"/"+nombreCentro);
                if(!centros.contains(idCentro))
                {
                    this.executeInsert("SELECT id FROM centro WHERE id='"+idCentro+"'",
                            "INSERT INTO centro(id,nombre) VALUES ('"+idCentro+"','"
                                    +nombreCentro+"')");
                    centros.add(idCentro);
                }
                
                //RELLENO DE TABLA OFICINA VENTAS
                String idOficina=sheet.getRow(ctRow).getCell(2).toString().toUpperCase();
                String nombreOficina=sheet.getRow(ctRow).getCell(3).toString();
                System.out.println("Of Ventas: "+idOficina+"/"+nombreOficina);
                if(!oficinas.contains(idOficina))
                {
                    this.executeInsert("SELECT id FROM oficinaVentas WHERE id='"+idOficina+"'",
                            "INSERT INTO oficinaVentas(id,nombre,centro_id) VALUES ('"+idOficina+"','"
                                    +nombreOficina+"','"+idCentro+"')");
                    oficinas.add(idOficina);
                }
                
                //RELLENO TABLA MATERIAL
                XSSFCell aux=sheet.getRow(ctRow).getCell(5);
                aux.setCellType(CellType.STRING);
                String idMaterial=aux.toString();
                String nombreMaterial=sheet.getRow(ctRow).getCell(6).toString();
                System.out.println("Centro: "+idMaterial+"/"+nombreMaterial);
                if(!materiales.contains(idMaterial))
                {
                    this.executeInsert("SELECT id FROM material WHERE id='"+idMaterial+"'",
                            "INSERT INTO material(id,nombre) VALUES ('"+idMaterial+"','"
                                    +nombreMaterial+"')");
                    materiales.add(idMaterial);
                }
                
                
              //  System.out.println("Pedido(Sector,Cj,Kg,$): "+sheet.getRow(ctRow).getCell(4)+"/"+sheet.getRow(ctRow).getCell(9)
              //          +"/"+sheet.getRow(ctRow).getCell(10)+"/"+sheet.getRow(ctRow).getCell(11));
                
              //  System.out.println("Fecha/TipoCliente: "+sheet.getRow(ctRow).getCell(7)+"/"+sheet.getRow(ctRow).getCell(8));
                System.out.println("-----------------------------");
                ctRow++;
            }
            this.close();
            
            
            
            
            /*
            Row row;
            // se recorre cada fila hasta el final
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                //se obtiene las celdas por fila
                Iterator<Cell> cellIterator = row.cellIterator();
                Cell cell;
                //se recorre cada celda
                while (cellIterator.hasNext()) {
                    // se obtiene la celda en específico y se la imprime
                    cell = (Cell) cellIterator.next();
                    System.out.print(cell.toString()+" | ");
                }
                System.out.println();
            }*/
        } catch (Exception e) {
            System.out.println("upsis :c"+e);
        }
        
        
        
        return false;
    }
}
