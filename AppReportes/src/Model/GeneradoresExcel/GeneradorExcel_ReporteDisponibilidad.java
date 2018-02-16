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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 *
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
    public boolean generarArchivo(HashMap<String, RecursoDB> resources)
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
            XSSFWorkbook libro = new XSSFWorkbook();
            //Se inicializa el flujo de datos con el archivo xls
            file = new FileOutputStream(archivoXLS);
            
            //Utilizamos la clase Sheet para crear una nueva hoja de trabajo dentro del libro que creamos anteriormente
            XSSFSheet hoja = libro.createSheet(this.nombreTabla);
            //inicialiar fila de nombres de columnas
            System.out.println("Creando nombres de columnas...");
            XSSFRow fila = hoja.createRow(0);
            if (this.columnas == null || this.columnas.isEmpty())
            {
                System.out.println("no esta inicializado encabezado columnas, at GenExcel-ReporteDisp");
                return false;
            }
            for (int i = 0; i < this.columnas.size(); i++)
            {
                XSSFCell celda = fila.createCell(i);
                celda.setCellValue(this.columnas.get(i));
                hoja.autoSizeColumn(i);
            }
            int contReg = 1;
            ArrayList<Recurso> recursosAux = resources.get(this.nombreTabla).getAll();
            for (int i = 0; i < recursosAux.size(); i++)
            {
                XSSFRow filaAux = hoja.createRow(i + 1);

                BaseReporteDisponibilidad row = (BaseReporteDisponibilidad) recursosAux.get(i);
                filaAux.createCell(0).setCellValue(row.centro_id);
                filaAux.createCell(1).setCellValue(row.centro_nombre);
                filaAux.createCell(2).setCellValue(row.sector_id);
                filaAux.createCell(3).setCellValue(row.sector_nombre);
                filaAux.createCell(4).setCellValue(row.agrupado_id);
                filaAux.createCell(5).setCellValue(row.agrupado_nombre);
                filaAux.createCell(6).setCellValue(row.fecha);
                filaAux.createCell(7).setCellValue(row.pedido_Cj);
                filaAux.createCell(8).setCellValue(row.despacho_Cj);
                filaAux.createCell(9).setCellValue(row.disponibleCj);
                filaAux.createCell(10).setCellValue(row.pedido_Kg);
                filaAux.createCell(11).setCellValue(row.pedido_neto);
                filaAux.createCell(12).setCellValue(row.disponibleKg);
                filaAux.createCell(13).setCellValue(row.faltanteCj);
                filaAux.createCell(14).setCellValue(row.faltanteKg);
                filaAux.createCell(15).setCellValue(row.semana);
                filaAux.createCell(16).setCellValue(row.sobranteCj);
                filaAux.createCell(17).setCellValue(row.sobranteKg);
                filaAux.createCell(18).setCellValue(row.faltanteDespachoCj);
                filaAux.createCell(19).setCellValue(row.faltanteAjustadoCj);
                filaAux.createCell(20).setCellValue(row.faltanteDespachoKg);
                filaAux.createCell(21).setCellValue(row.faltanteAjustadoKg);
                filaAux.createCell(22).setCellValue(row.diaSemana);
                filaAux.createCell(23).setCellValue(row.anio);
                contReg++;
            }
            libro.write(file);
            file.close();
            if (contReg == 1)
            {
                //eliminar archivo
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
