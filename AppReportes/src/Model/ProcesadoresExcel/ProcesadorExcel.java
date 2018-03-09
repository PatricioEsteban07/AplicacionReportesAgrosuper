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
 * Clase abstracta para los objetos de tipo ProcesadorExcel
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
    
    /**
     * Método para, dado un título de una columna del reporte, buscar su ubicación en una hoja en espcífico.
     * @param hoja contiene la hoja Excel a buscar lo solicitado.
     * @param tituloColumna contiene el contenido del título de la celda a buscar.
     * @param rowTitulos valor numérico que define la fila en donde se almacenan los títulos.
     * @param colInicio valor numérico que define la columna inicial para la búsqueda.
     * @return valor mayor o igual a 0 cuando se encuentra la columna asociada al título buscado, y -1 en caso contrario.
     */
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
            }
            ctCol++;
        }
        return -1;
    }

    /**
     * Método para abrir un archivo ubicado en la dirección fileDir.
     * @param fileDir contiene la dirección del archivo a abrir.
     * @return un archivo si se encuentra o null en caso contrario.
     */
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
    
    /**
     * Método encargado de llamar al método de obtención de archivos CSV de acuerdo al tipo de archivo Excel que se 
     * está procesando (.XLS o .XLSX).
     * @return true si la generación de archivos CSV fue exitosa, o false en caso contrario.
     */
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
    
    /**
     * Método encargado de llamar al método de obtención de archivos CSV para archivos .XLSX
     * @return true si la generación de archivos CSV fue exitosa, o false en caso contrario.
     */
    public abstract boolean obtieneDatosXLSX();
    
    
    /**
     * Método encargado de llamar al método de obtención de archivos CSV para archivos .XLS
     * @return true si la generación de archivos CSV fue exitosa, o false en caso contrario.
     */
    public abstract boolean obtieneDatosXLS();
    
}
