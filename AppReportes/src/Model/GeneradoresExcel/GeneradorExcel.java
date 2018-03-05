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
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/**
 *
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
    
    protected HSSFFont createFont(HSSFWorkbook workbook, short fontColor, short fontHeight, short fontBold) {
        HSSFFont font = workbook.createFont();
      //  font.setBoldweight(fontBold);
        font.setColor(fontColor);
        font.setFontName("Arial");
        font.setFontHeightInPoints(fontHeight);

        return font;
    }
    /*
    protected HSSFCellStyle createStyle(HSSFWorkbook workbook, HSSFFont font, short cellAlign, short cellColor, boolean cellBorder, short cellBorderColor) 
    {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(cellAlign);
        style.setFillForegroundColor(cellColor);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        if (cellBorder) {
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);

            style.setTopBorderColor(cellBorderColor);
            style.setLeftBorderColor(cellBorderColor);
            style.setRightBorderColor(cellBorderColor);
            style.setBottomBorderColor(cellBorderColor);
        }
        return style;
    }
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
    
    public abstract boolean generarArchivo(HashMap<String,RecursoDB> resources, String fileDir) throws FileNotFoundException, IOException;
    
}
