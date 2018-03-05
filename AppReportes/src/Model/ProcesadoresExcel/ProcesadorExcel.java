/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ProcesadoresExcel;

import java.io.File;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author Patricio
 */
public abstract class ProcesadorExcel
{
    public static final String EXCEL_XLS = "XLS";
    public static final String EXCEL_XLSX = "XLSX";

    public String fileDir;
    public String nombreHoja;
    public String tipo;

    public ProcesadorExcel(String fileDir, String nombreHoja)
    {
        this.fileDir = fileDir;
        this.nombreHoja=nombreHoja;
        this.tipo = this.fileDir.toUpperCase().substring(this.fileDir.length() - 4).replace(".", "");
    }
    
    public int buscaColumna(Sheet hoja, String tituloColumna, int rowTitulos, int colInicio)
    {
        Row row = hoja.getRow(rowTitulos);
        int ctCol = colInicio;
        Iterator<Cell> cellIterator = row.cellIterator();
        Cell celda;
        while (cellIterator.hasNext())
        {
            celda = cellIterator.next();
            if (ctCol >= colInicio)
            {
                if (celda.getStringCellValue().equals(tituloColumna))
                {
                    return ctCol;
                }
                //System.out.println("C: "+celda.getStringCellValue());
            }
            ctCol++;
        }
        return -1;
    }

    protected File abrirArchivo(String fileDir)
    {
        File archivoExcel = new File(fileDir);
        if (!archivoExcel.exists())
        {
            System.out.println("Ojo, el archivo no existe :c");
            return null;
        }
        return archivoExcel;
    }
    
    public boolean generarCSV()
    {
        switch(this.tipo)
        {
            case EXCEL_XLSX:
                return obtieneDatosXLSX();
            case EXCEL_XLS:
                return obtieneDatosXLS();
        }
        System.out.println("ojo, tipo no reconocido");
        return false;
    }
    
    public abstract boolean obtieneDatosXLSX();
    public abstract boolean obtieneDatosXLS();
    
}
