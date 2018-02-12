/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.GeneradoresExcel;

import Model.Recurso;
import Model.RecursosDB.RecursoDB;
import Model.Reportes.BaseReporteArbolPerdidas;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


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
        HashMap<String, BaseReporteArbolPerdidas> filas = new HashMap<>();
        
        //parte 1 : tabla básica
            //sacar centro (se hizo anteriormente)
            //sacar sector_material
            //sacar agrupacion material
            //sacar semana/diaSemana/año
                
        //generación de archivo excel base
        String rutaArchivo = System.getProperty("user.home")+"/Desktop/"+this.nombreTabla+".xlsx";
        File archivoXLS = new File(rutaArchivo);
        
        //Se crea el libro de excel usando el objeto de tipo Workbook
        XSSFWorkbook  libro = new XSSFWorkbook ();
        //Se inicializa el flujo de datos con el archivo xls
        FileOutputStream file = new FileOutputStream(archivoXLS);
        /*
        // Generate fonts
        HSSFFont headerFont  = createFont(libro,HSSFColor.WHITE.index, (short)12, );
        HSSFFont contentFont = createFont(libro,HSSFColor.BLACK.index, (short)10, Font.BOLDWEIGHT_NORMAL);

        // Generate styles
        HSSFCellStyle headerStyle  = createStyle(libro,headerFont,  HSSFCellStyle.ALIGN_CENTER, HSSFColor.BLUE_GREY.index,       true, HSSFColor.WHITE.index);
        HSSFCellStyle oddRowStyle  = createStyle(libro,contentFont, HSSFCellStyle.ALIGN_LEFT,   HSSFColor.WHITE.index, true, HSSFColor.GREY_80_PERCENT.index);
        HSSFCellStyle evenRowStyle = createStyle(libro,contentFont, HSSFCellStyle.ALIGN_LEFT,   HSSFColor.GREY_25_PERCENT.index, true, HSSFColor.GREY_80_PERCENT.index);
        */
        //Utilizamos la clase Sheet para crear una nueva hoja de trabajo dentro del libro que creamos anteriormente
        XSSFSheet  hoja = libro.createSheet(this.nombreTabla);
                
        //inicialiar fila de nombres de columnas
        System.out.println("Creando nombres de columnas...");        
        XSSFRow  fila = hoja.createRow( 0 );
        if(this.columnas==null || this.columnas.isEmpty())
        {
            System.out.println("no esta inicializado encabezado columnas, at GenExcel-ReporteDisp");
            return false;
        }
        for (int i = 0; i < this.columnas.size(); i++)
        {
            XSSFCell  celda = fila.createCell(i);
//            celda.setCellStyle(headerStyle);
            celda.setCellValue(this.columnas.get(i));
            hoja.autoSizeColumn(i);
        }
        
        System.out.println("Rellenando tabla con valores...");
        
        int contReg=1;
        ArrayList<Recurso> recursosAux = resources.get(this.nombreTabla).getAll();
        for (int i=0; i<recursosAux.size();i++)
        { 
            XSSFRow filaAux = hoja.createRow(i+1);
                
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
        
        System.out.println("Registros CE: "+(contReg-1));
        libro.write(file);
        file.close();
        if(contReg==1)
        {
            System.out.println("OJO, NO HAY REGISTROS A CARGAR!");
            //eliminar archivo
            archivoXLS = new File(rutaArchivo);
            archivoXLS.delete();
            return false;
        }
        return true;
    }

    
}
