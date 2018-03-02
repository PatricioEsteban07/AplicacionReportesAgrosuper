/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.GeneradoresExcel;

import Model.CommandNames;
import Model.Recurso;
import Model.RecursosDB.RecursoDB;
import Model.Reportes.BaseReporteFugaFS;
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
 *
 * @author Patricio
 */
public class GeneradorExcel_ReporteFugaFS extends GeneradorExcel
{
    
    public GeneradorExcel_ReporteFugaFS(ArrayList<String> columnas)
    {
        super("Reporte Fuga FS");
        this.columnas=columnas;
    }

    @Override
    public boolean generarArchivo(HashMap<String, RecursoDB> resources)
    {
        FileOutputStream file = null;
        try
        {
            HashMap<String, BaseReporteFugaFS> filas = new HashMap<>();
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
                System.out.println("no esta inicializado encabezado columnas, at GenExcel-ReporteFugaFS");
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

                BaseReporteFugaFS row = (BaseReporteFugaFS) recursosAux.get(i);
                setCellContent(filaAux,0,row.clienteLocal_id,CellType.STRING);
                setCellContent(filaAux,1,row.clienteLocal_nombre,CellType.STRING);
                setCellContent(filaAux,2,row.comuna,CellType.STRING);
                setCellContent(filaAux,3,row.direccion,CellType.STRING);
                setCellContent(filaAux,4,row.cadena,CellType.STRING);
                setCellContent(filaAux,5,row.categoriaCliente,CellType.STRING);
                setCellContent(filaAux,6,row.subcategoriaCliente,CellType.STRING);
                setCellContent(filaAux,7,row.sucursal,CellType.STRING);
                setCellContent(filaAux,8,row.zonaVenta,CellType.STRING);
                setCellContent(filaAux,9,row.supervisor,CellType.STRING);
                setCellContent(filaAux,10,row.preventa,CellType.STRING);
                setCellContent(filaAux,11,row.kam,CellType.STRING);
                setCellContent(filaAux,12,row.centralizado,CellType.STRING);
                setCellContent(filaAux,13,row.agcnc,CellType.STRING);
                setCellContent(filaAux,14,row.año,CellType.NUMERIC);
                setCellContent(filaAux,15,row.mes,CellType.NUMERIC);
                setCellContent(filaAux,16,row.ejecutivo,CellType.STRING);
                setCellContent(filaAux,17,row.diaLlamado,CellType.STRING);
                setCellContent(filaAux,18,row.tipoClub,CellType.STRING);
                setCellContent(filaAux,19,row.categoriaClub,CellType.STRING);
                setCellContent(filaAux,20,row.segmentoClub,CellType.STRING);
                setCellContent(filaAux,21,row.canje,CellType.STRING);
                setCellContent(filaAux,22,row.clienteFugado,CellType.NUMERIC);
                setCellContent(filaAux,23,row.clienteHistorico,CellType.NUMERIC);
                setCellContent(filaAux,23,row.clienteNuevo,CellType.NUMERIC);
                setCellContent(filaAux,23,row.clienteVigente,CellType.NUMERIC);
                setCellContent(filaAux,23,row.clienteRecuperado,CellType.NUMERIC);
                setCellContent(filaAux,23,row.clienteFugaNeto,CellType.NUMERIC);
                setCellContent(filaAux,23,row.clienteCrecimientoNeto,CellType.NUMERIC);
                setCellContent(filaAux,23,row.clienteRecuperadoKg,CellType.NUMERIC);
                setCellContent(filaAux,23,row.clienteFugaKg,CellType.NUMERIC);
                setCellContent(filaAux,23,row.clienteCrecimientoKg,CellType.NUMERIC);
                setCellContent(filaAux,23,row.brechaFuga_neto,CellType.NUMERIC);
                setCellContent(filaAux,23,row.brechaCrecimiento_neto,CellType.NUMERIC);
                setCellContent(filaAux,23,row.brecha_neto,CellType.NUMERIC);
                setCellContent(filaAux,23,row.brechaFuga_Kg,CellType.NUMERIC);
                setCellContent(filaAux,23,row.brechaCrecimiento_Kg,CellType.NUMERIC);
                setCellContent(filaAux,23,row.brecha_Kg,CellType.NUMERIC);
                setCellContent(filaAux,23,row.ventaClientesFugados,CellType.NUMERIC);
                setCellContent(filaAux,23,row.ventaClientesHistoricos,CellType.NUMERIC);
                setCellContent(filaAux,23,row.ventaClientesNuevos,CellType.NUMERIC);
                setCellContent(filaAux,23,row.ventaClientesVigentes,CellType.NUMERIC);
                setCellContent(filaAux,23,row.clientesFugados_Kg,CellType.NUMERIC);
                setCellContent(filaAux,23,row.clientesHistoricos_Kg,CellType.NUMERIC);
                setCellContent(filaAux,23,row.clientesNuevos_Kg,CellType.NUMERIC);
                setCellContent(filaAux,23,row.clientesVigentes_Kg,CellType.NUMERIC);
                setCellContent(filaAux,23,row.tipoCall,CellType.STRING);
                setCellContent(filaAux,23,row.kamJr,CellType.STRING);
                setCellContent(filaAux,23,row.jefeVentas,CellType.STRING);
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
            GeneradorExcel_ReporteFugaFS.copyFile(archivoXLS, new File(System.getProperty("user.home") + "/Desktop/" + this.nombreTabla + ".xlsx"));
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
