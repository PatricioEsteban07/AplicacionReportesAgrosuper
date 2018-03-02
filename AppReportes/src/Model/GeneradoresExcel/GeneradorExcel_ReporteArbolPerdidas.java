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
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFCell;
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
                CommandNames.generaMensaje("Error de Sistema", Alert.AlertType.ERROR, "Error inicializando columnas Reporte",
                    "OJO, ENCABEZADO COLUMNAS NO INICIALIZADO. Avisar al informático.");
                System.out.println("no esta inicializado encabezado columnas, at GenExcel-ReporteArbolPerdidas");
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

                BaseReporteArbolPerdidas row = (BaseReporteArbolPerdidas)recursosAux.get(i);
                setCellContent(filaAux,0,row.mes,CellType.NUMERIC);
                setCellContent(filaAux,1,row.semana,CellType.NUMERIC);
                setCellContent(filaAux,2,row.sector_nombre,CellType.STRING);
                setCellContent(filaAux,3,row.tipoCliente,CellType.STRING);
                setCellContent(filaAux,4,row.centro_id,CellType.STRING);
                setCellContent(filaAux,5,row.centro_nombre,CellType.STRING);
                setCellContent(filaAux,6,row.agrupado_id,CellType.STRING);
                setCellContent(filaAux,7,row.agrupado_nombre,CellType.STRING);
                setCellContent(filaAux,8,row.n2_nombre,CellType.STRING);
                setCellContent(filaAux,9,row.Pedido_Kg,CellType.NUMERIC);
                setCellContent(filaAux,10,row.Factura_Kg,CellType.NUMERIC);
                setCellContent(filaAux,11,row.Demanda_Kg,CellType.NUMERIC);
                setCellContent(filaAux,12,row.NS_Kg,CellType.NUMERIC);
                setCellContent(filaAux,13,row.Faltante_Kg,CellType.NUMERIC);
                setCellContent(filaAux,14,row.Sobrefactura_Kg,CellType.NUMERIC);
                setCellContent(filaAux,15,row.PP_Neto,CellType.NUMERIC);
                setCellContent(filaAux,16,row.Faltante_Neto,CellType.NUMERIC);
                setCellContent(filaAux,17,row.Pedido_Cj,CellType.NUMERIC);
                setCellContent(filaAux,18,row.Factura_Cj,CellType.NUMERIC);
                setCellContent(filaAux,19,row.Demanda_Cj,CellType.NUMERIC);
                setCellContent(filaAux,20,row.NS_Cj,CellType.NUMERIC);
                setCellContent(filaAux,21,row.Sobrefactura_Cj,CellType.NUMERIC);
                setCellContent(filaAux,22,row.Faltante_Cj,CellType.NUMERIC);
                setCellContent(filaAux,23,row.Disp_Pedido_Cj,CellType.NUMERIC);
                setCellContent(filaAux,24,row.Disp_Faltante_Cj,CellType.NUMERIC);
                setCellContent(filaAux,25,row.Disp_Pedido_Kg,CellType.NUMERIC);
                setCellContent(filaAux,26,row.Disp_Faltante_Kg,CellType.NUMERIC);
                setCellContent(filaAux,27,row.Factura_Faltante_Kg,CellType.NUMERIC);
                setCellContent(filaAux,28,row.Factura_Faltante_Cj,CellType.NUMERIC);
                setCellContent(filaAux,29,row.Pedido_Neto,CellType.NUMERIC);
                setCellContent(filaAux,30,row.Anio,CellType.NUMERIC);
                setCellContent(filaAux,31,row.semanaAnio,CellType.STRING);
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
            GeneradorExcel_ReporteDisponibilidad.copyFile(archivoXLS, new File(System.getProperty("user.home") + "/Desktop/" + this.nombreTabla + ".xlsx"));
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

    private void setCellContent(SXSSFRow row, int i, String content, CellType cellType)
    {
        SXSSFCell cAux=row.createCell(i);
       // System.out.println("Style:"+cAux.getCellStyle().getDataFormat());
        cAux.setCellType(cellType);    
        cAux.setCellValue(content);
    }

}
