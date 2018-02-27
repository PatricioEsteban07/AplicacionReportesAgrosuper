/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ProcesadoresExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Patricio
 */
public class ProcesadorExcel
{
    public static final String EXCEL_XLS = "XLS";
    public static final String EXCEL_XLSX = "XLSX";
    
    public String fileDir;
    public String tipo;

    public ProcesadorExcel(String fileDir)
    {
        this.fileDir = fileDir;
        this.tipo = this.fileDir.toUpperCase().substring(this.fileDir.length()-4)
                .replace(".", "");
    }
    
    
    
    
    
    public int buscaColumna(String nombreHoja, String tituloColumna, int rowTitulos, int colInicio)
    {
        return (this.tipo.equals(EXCEL_XLS)) ? buscaColumnaXLS(nombreHoja,tituloColumna,rowTitulos,colInicio) 
                : buscaColumnaXLSX(nombreHoja,tituloColumna,rowTitulos,colInicio) ;
    }
    
    private int obtieneDatos(String nombreHoja, String tituloColumna, int rowTitulos, int colInicio)
    {
        FileInputStream file = null;
        try
        {
            file = new FileInputStream(new File("C:\\prueb_excel.xls"));
            // Crear el objeto que tendra el libro de Excel
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            
            HSSFSheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(rowTitulos);
                            
            int ctAux=colInicio;
            // Obtenemos el iterator que permite recorres todas las celdas de una fila
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()){
                Cell celda = cellIterator.next();
                if(true)
                    return ctAux;
                ctAux++;
            }
            
            workbook.close();
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(ProcesadorExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ProcesadorExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                file.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(ProcesadorExcel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return -1;
    }
    
    private int buscaColumnaXLS(String nombreHoja, String tituloColumna, int rowTitulos, int colInicio)
    {
        
        return -1;
    }
    private int buscaColumnaXLSX(String nombreHoja, String tituloColumna, int rowTitulos, int colInicio)
    {
        return -1;
    }
}
