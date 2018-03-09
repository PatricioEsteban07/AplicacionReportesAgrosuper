/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.GeneradoresExcel;

import Model.CommandNames;
import Model.Recurso;
import Model.RecursosDB.RecursoDB;
import Model.Reportes.BaseReporteDisponibilidad;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.Alert;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


/**
 * Clase encargada de la generación de un archivo Excel para el reporte Disponibilidad
 * @author Patricio
 */
public class GeneradorExcel_ReporteDisponibilidad extends GeneradorExcel
{
    
    public GeneradorExcel_ReporteDisponibilidad(ArrayList<String> columnas)
    {
        super("Reporte Disponibilidad");
        this.columnas=columnas;
    }

    @Override
    public boolean generarArchivo(HashMap<String, RecursoDB> resources, String fileDir)
    {
        FileOutputStream file = null;
        try
        {
            HashMap<String, BaseReporteDisponibilidad> filas = new HashMap<>();
            String rutaArchivo = System.getProperty("java.io.tmpdir") + "/" + this.nombreTabla + ".xlsx";
            File archivoXLS = new File(rutaArchivo);
            SXSSFWorkbook libro = new SXSSFWorkbook();
            file = new FileOutputStream(archivoXLS);
            SXSSFSheet hoja = libro.createSheet(this.nombreTabla);
            SXSSFRow fila = hoja.createRow(0);
            if (this.columnas == null || this.columnas.isEmpty())
            {
                CommandNames.generaMensaje("Error de Sistema", Alert.AlertType.ERROR, "Error inicializando columnas Reporte",
                    "OJO, ENCABEZADO COLUMNAS NO INICIALIZADO. Avisar al informático.");
                System.out.println("no esta inicializado encabezado columnas, at GenExcel-ReporteDisp");
                return false;
            }
            for (int i = 0; i < this.columnas.size(); i++)
            {
                SXSSFCell celda = fila.createCell(i);
                celda.setCellValue(this.columnas.get(i));
            }
            int contReg = 1;
            ArrayList<Recurso> recursosAux = resources.get(this.nombreTabla).getAll();
            for (int i = 0; i < recursosAux.size(); i++)
            {
                SXSSFRow filaAux = hoja.createRow(i + 1);

                BaseReporteDisponibilidad row = (BaseReporteDisponibilidad) recursosAux.get(i);
                setCellContent(filaAux,0,row.centro_id,CellType.STRING);
                setCellContent(filaAux,1,row.centro_nombre,CellType.STRING);
                setCellContent(filaAux,2,row.sector_id,CellType.STRING);
                setCellContent(filaAux,3,row.sector_nombre,CellType.STRING);
                setCellContent(filaAux,4,row.agrupado_id,CellType.STRING);
                setCellContent(filaAux,5,row.agrupado_nombre,CellType.STRING);
                setCellContent(filaAux,6,row.fecha,CellType.STRING);
                setCellContent(filaAux,7,row.pedido_Cj,CellType.NUMERIC);
                setCellContent(filaAux,8,row.despacho_Cj,CellType.NUMERIC);
                setCellContent(filaAux,9,row.disponibleCj,CellType.NUMERIC);
                setCellContent(filaAux,10,row.pedido_Kg,CellType.NUMERIC);
                setCellContent(filaAux,11,row.pedido_neto,CellType.NUMERIC);
                setCellContent(filaAux,12,row.disponibleKg,CellType.NUMERIC);
                setCellContent(filaAux,13,row.faltanteCj,CellType.NUMERIC);
                setCellContent(filaAux,14,row.faltanteKg,CellType.NUMERIC);
                setCellContent(filaAux,15,row.semana,CellType.NUMERIC);
                setCellContent(filaAux,16,row.sobranteCj,CellType.NUMERIC);
                setCellContent(filaAux,17,row.sobranteKg,CellType.NUMERIC);
                setCellContent(filaAux,18,row.faltanteDespachoCj,CellType.NUMERIC);
                setCellContent(filaAux,19,row.faltanteAjustadoCj,CellType.NUMERIC);
                setCellContent(filaAux,20,row.faltanteDespachoKg,CellType.NUMERIC);
                setCellContent(filaAux,21,row.faltanteAjustadoKg,CellType.NUMERIC);
                setCellContent(filaAux,22,row.diaSemana,CellType.NUMERIC);
                setCellContent(filaAux,23,row.anio,CellType.NUMERIC);
                contReg++;
            }
            libro.write(file);
            file.close();
            if (contReg == 1)
            {
                //eliminar archivo
                CommandNames.generaMensaje("Aviso de Reporte", Alert.AlertType.INFORMATION, "Problema al generar Reporte",
                    "No existen registros para generar un archivo. El Reporte no se generará.");
                archivoXLS = new File(rutaArchivo);
                archivoXLS.delete();
                return false;
            }
            GeneradorExcel_ReporteDisponibilidad.copyFile(archivoXLS, new File(fileDir + "/" + this.nombreTabla + ".xlsx"));
            file.close();            
            archivoXLS = new File(rutaArchivo);
            archivoXLS.delete();
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
