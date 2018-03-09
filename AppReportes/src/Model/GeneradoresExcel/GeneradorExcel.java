/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.GeneradoresExcel;

import Model.RecursosDB.RecursoDB;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;

/**
 * Clase abstracta para los objetos de tipo GeneradorExcel
 * @author Patricio
 */
public abstract class GeneradorExcel
{
    public String nombreTabla;
    public ArrayList<String> columnas;

    public GeneradorExcel(String nombreTabla)
    {
        this.nombreTabla = nombreTabla;
        this.columnas = new ArrayList<>();
    }
    
    /**
     * Método estático que, dado una dirección fuente y una dirección destino, genera una copia del archivo fuente en la 
     * dirección destino.
     * @param sourceFile la dirección del archivo fuente.
     * @param destFile la dirección donde se almacenará la copia.
     * @throws IOException 
     */
    public static void copyFile(File sourceFile, File destFile) throws IOException 
    {
        if(!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel origen = null;
        FileChannel destino = null;
        try {
            origen = new FileInputStream(sourceFile).getChannel();
            destino = new FileOutputStream(destFile).getChannel();

            long count = 0;
            long size = origen.size();              
            while((count += destino.transferFrom(origen, count, size-count))<size);
        }
        finally {
            if(origen != null) {
                origen.close();
            }
            if(destino != null) {
                destino.close();
            }
        }
    }
    
    /**
     * Método que se encarga de crear una celda Excel con un contenido especificado por content, en una fila en específico.
     * @param row contiene la fila de un libro en especifico donde se desea guardar el contenido.
     * @param i el número de columna de la fila dondese desea guardar contenido.
     * @param content el contenido que se guardará en una celda Excel.
     * @param cellType el tipo de dato que se desea guardar (POR IMPLEMENTAR)
     */
    public void setCellContent(SXSSFRow row, int i, String content, CellType cellType)
    {
        SXSSFCell cAux=row.createCell(i);
        cAux.setCellType(cellType);    
        cAux.setCellValue(content);
    }
    
    /**
     * Método abstracto el cuál se encarga de, dado un conjunto de datos, generar el archivo Excel correspondiente de 
     * acuerdo al GeneradorExcel utilizado.
     * @param resources es el HashMap que posee para este caso, el ResultSet anteriormente generado por otro método, el cual 
     * contiene los datos para completar el archivo excel.
     * @param fileDir la dirección donde se guardará el archivo Excel generado.
     * @return true si el archivo fué generado exitosamente, o false en otro caso.
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public abstract boolean generarArchivo(HashMap<String,RecursoDB> resources, String fileDir) throws FileNotFoundException, IOException;
    
}
