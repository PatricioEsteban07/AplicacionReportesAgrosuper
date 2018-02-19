/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.GeneradoresExcel;

import Model.CommandNames;
import Model.Recurso;
import Model.RecursosDB.RecursoDB;
import Model.Reportes.BaseReporteArbolPerdidas;
import Model.Reportes.BaseReporteDisponibilidad;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.Alert;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


/**
 *
 * @author Patricio
 */
public class GeneradorExcel_ReporteArbolPerdidas extends GeneradorExcel
{
    
    public GeneradorExcel_ReporteArbolPerdidas(ArrayList<String> columnas)
    {
        super("Reporte Árbol Pérdidas");
        this.columnas=columnas;
    }

    @Override
    public boolean generarArchivo(HashMap<String,RecursoDB> resources) throws FileNotFoundException, IOException
    {
        FileOutputStream file = null;
        try
        {
            HashMap<String, BaseReporteDisponibilidad> filas = new HashMap<>();
            //generación de archivo excel base
            // System.out.println("El directorio temporal del sistema es "+System.getProperty("java.io.tmpdir"));
            String rutaArchivo = System.getProperty("java.io.tmpdir") + "/" + this.nombreTabla + ".xlsx";
            // String rutaArchivo = System.getProperty("user.home")+"/Desktop/"+this.nombreTabla+".xlsx";
            File archivoXLS = new File(rutaArchivo);
            //Se crea el libro de excel usando el objeto de tipo Workbook
            SXSSFWorkbook libro = new SXSSFWorkbook();
            //Se inicializa el flujo de datos con el archivo xls
            file = new FileOutputStream(archivoXLS);
            //Utilizamos la clase Sheet para crear una nueva hoja de trabajo dentro del libro que creamos anteriormente
            SXSSFSheet hoja = libro.createSheet(this.nombreTabla);
            //inicialiar fila de nombres de columnas
            SXSSFRow fila = hoja.createRow(0);
            if (this.columnas == null || this.columnas.isEmpty())
            {
                CommandNames.generaMensaje("X", Alert.AlertType.INFORMATION, "X",
                    "OJO, ENCABEZADO COLUMNAS NO INICIALIZADO");
                System.out.println("no esta inicializado encabezado columnas, at GenExcel-ReporteArbol");
                return false;
            }
            for (int i = 0; i < this.columnas.size(); i++)
            {
                Cell celda = fila.createCell(i);
                celda.setCellValue(this.columnas.get(i));
            }
            int contReg=1;
            ArrayList<Recurso> recursosAux = resources.get(this.nombreTabla).getAll();
            for (int i=0; i<recursosAux.size();i++)
            { 
                Row filaAux = hoja.createRow(i + 1);

                BaseReporteArbolPerdidas row = (BaseReporteArbolPerdidas)recursosAux.get(i);
                filaAux.createCell(0).setCellValue(row.mes);
                filaAux.createCell(1).setCellValue(row.semana);
                filaAux.createCell(2).setCellValue(row.sector_nombre);
                filaAux.createCell(3).setCellValue(row.tipoCliente);
                filaAux.createCell(4).setCellValue(row.centro_id);
                filaAux.createCell(5).setCellValue(row.centro_nombre);
                filaAux.createCell(6).setCellValue(row.agrupado_id);
                filaAux.createCell(7).setCellValue(row.agrupado_nombre);
                filaAux.createCell(8).setCellValue(row.n2_nombre);
                filaAux.createCell(9).setCellValue(row.Pedido_Kg);
                filaAux.createCell(10).setCellValue(row.Factura_Kg);
                filaAux.createCell(11).setCellValue(row.Demanda_Kg);
                filaAux.createCell(12).setCellValue(row.NS_Kg);
                filaAux.createCell(13).setCellValue(row.Faltante_Kg);
                filaAux.createCell(14).setCellValue(row.Sobrefactura_Kg);
                filaAux.createCell(15).setCellValue(row.PP_Neto);
                filaAux.createCell(16).setCellValue(row.Faltante_Neto);
                filaAux.createCell(17).setCellValue(row.Pedido_Cj);
                filaAux.createCell(18).setCellValue(row.Factura_Cj);
                filaAux.createCell(19).setCellValue(row.Demanda_Cj);
                filaAux.createCell(20).setCellValue(row.NS_Cj);
                filaAux.createCell(21).setCellValue(row.Sobrefactura_Cj);
                filaAux.createCell(22).setCellValue(row.Faltante_Cj);
                filaAux.createCell(23).setCellValue(row.Disp_Pedido_Cj);
                filaAux.createCell(24).setCellValue(row.Disp_Faltante_Cj);
                filaAux.createCell(25).setCellValue(row.Disp_Pedido_Kg);
                filaAux.createCell(26).setCellValue(row.Disp_Faltante_Kg);
                filaAux.createCell(27).setCellValue(row.Factura_Faltante_Kg);
                filaAux.createCell(28).setCellValue(row.Factura_Faltante_Cj);
                filaAux.createCell(29).setCellValue(row.Pedido_Neto);
                filaAux.createCell(30).setCellValue(row.Anio);
                filaAux.createCell(31).setCellValue(row.semanaAnio);
                contReg++;
            }
            libro.write(file);
            file.close();
            if (contReg == 1)
            {
                //eliminar archivo
                CommandNames.generaMensaje("Aviso de Reporte", Alert.AlertType.ERROR, "Problema al generar Reporte",
                    "No existen registros para generar un archivo. El Reporte no se generará.");
                archivoXLS = new File(rutaArchivo);
                archivoXLS.delete();
                return false;
            }
            this.copyFile(archivoXLS, new File(System.getProperty("user.home") + "/Desktop/" + this.nombreTabla + ".xlsx"));
            file.close();
            return true;
        }
        catch (IOException ex)
        {
            CommandNames.generaMensaje("Aviso de Reporte", Alert.AlertType.ERROR, "Problema al generar Reporte",
                    "El error es el siguiente: " + ex);
            return false;
        }
    }

    
}
