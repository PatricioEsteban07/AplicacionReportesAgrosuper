/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.GeneradoresExcel;

import Model.RecursosDB.RecursoDB;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
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
    
    public abstract boolean generarArchivo(HashMap<String,RecursoDB> resources) throws FileNotFoundException, IOException;
    
    protected HSSFFont createFont(HSSFWorkbook workbook, short fontColor, short fontHeight, short fontBold) {
        HSSFFont font = workbook.createFont();
        font.setBoldweight(fontBold);
        font.setColor(fontColor);
        font.setFontName("Arial");
        font.setFontHeightInPoints(fontHeight);

        return font;
    }
    
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
}
