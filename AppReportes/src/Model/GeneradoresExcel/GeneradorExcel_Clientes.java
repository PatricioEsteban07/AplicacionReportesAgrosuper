/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.GeneradoresExcel;

import Model.Cliente;
import Model.Recurso;
import Model.RecursosDB.RecursoDB;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;

/**
 *
 * @author Patricio
 */
public class GeneradorExcel_Clientes extends GeneradorExcel
{

    public GeneradorExcel_Clientes()
    {
        super("Clientes");
        this.columnas.add("cliente_id");
        this.columnas.add("cliente_nombre");
        this.columnas.add("cliente_apellido");
        this.columnas.add("cliente_edad");
        this.columnas.add("cliente_sexo");
        this.columnas.add("cliente_descripcion");
    }

    @Override
    public boolean generarArchivo(HashMap<String, RecursoDB> resources) throws FileNotFoundException, IOException
    {
        //generación de archivo excel base
        String rutaArchivo = System.getProperty("user.home") + "/Desktop/" + nombreTabla + ".xls";
        File archivoXLS = new File(rutaArchivo);
        
        /*
        int rowInicial=0;
        //OJO EN ESTA PARTE, A FUTURO REUTILIZAR ARCHIVOS GENERADOS
        
        if (archivoXLS.exists())
        {
            HSSFWorkbook libro = new HSSFWorkbook(new FileInputStream(rutaArchivo));
            HSSFSheet hoja = libro.createSheet(nombreTabla);
            for (Row row : hoja)
            {
                // Si falta la celda del archivo, genera una casilla en blanco
                Cell cell = row.getCell(0, Row.CREATE_NULL_AS_BLANK);

                // Imprimir la celda para depurar
                System.out.println("CELL: " + 0 + " --> " + cell.toString());
                rowInicial++;
            }
        }
        archivoXLS.delete();
        archivoXLS.createNewFile();
*/
        
        //Se crea el libro de excel usando el objeto de tipo Workbook
        HSSFWorkbook libro = new HSSFWorkbook();
        //Se inicializa el flujo de datos con el archivo xls
        FileOutputStream file = new FileOutputStream(archivoXLS);

        // Generate fonts   
        HSSFFont headerFont = createFont(libro, HSSFColor.WHITE.index, (short) 12, Font.BOLDWEIGHT_BOLD);
        HSSFFont contentFont = createFont(libro, HSSFColor.BLACK.index, (short) 10, Font.BOLDWEIGHT_NORMAL);

        // Generate styles
        HSSFCellStyle headerStyle = createStyle(libro, headerFont, HSSFCellStyle.ALIGN_CENTER, HSSFColor.BLUE_GREY.index, true, HSSFColor.WHITE.index);
        HSSFCellStyle oddRowStyle = createStyle(libro, contentFont, HSSFCellStyle.ALIGN_LEFT, HSSFColor.WHITE.index, true, HSSFColor.GREY_80_PERCENT.index);
        HSSFCellStyle evenRowStyle = createStyle(libro, contentFont, HSSFCellStyle.ALIGN_LEFT, HSSFColor.GREY_25_PERCENT.index, true, HSSFColor.GREY_80_PERCENT.index);

        //Utilizamos la clase Sheet para crear una nueva hoja de trabajo dentro del libro que creamos anteriormente
        HSSFSheet hoja = libro.createSheet(nombreTabla);

        //inicialiar fila de nombres de columnas
        System.out.println("Creando nombres de columnas...");
        HSSFRow fila = hoja.createRow(0);
        for (int i = 0; i < this.columnas.size(); i++)
        {
            HSSFCell celda = fila.createCell(i);
            celda.setCellStyle(headerStyle);
            celda.setCellValue(this.columnas.get(i));
            hoja.autoSizeColumn(i);
        }

        ArrayList<Recurso> clientes = resources.get("Clientes").getAll();

        System.out.println("Rellenando tabla con valores...");
        for (int i = 0; clientes != null && i < clientes.size(); i++)
        {
            //La clase Row nos permitirá crear las filas
            HSSFRow filaAux = hoja.createRow(i + 1);

            //Creamos la celda a partir de la fila actual
            HSSFCell celda = filaAux.createCell(0);
            celda.setCellValue(clientes.get(i).id);
            celda.setCellStyle(i % 2 == 0 ? oddRowStyle : evenRowStyle);
            celda = filaAux.createCell(1);
            celda.setCellValue(((Cliente) (clientes.get(i))).nombre);
            celda.setCellStyle(i % 2 == 0 ? oddRowStyle : evenRowStyle);
            celda = filaAux.createCell(2);
            celda.setCellValue(((Cliente) (clientes.get(i))).apellido);
            celda.setCellStyle(i % 2 == 0 ? oddRowStyle : evenRowStyle);
            celda = filaAux.createCell(3);
            celda.setCellValue(((Cliente) (clientes.get(i))).edad);
            celda.setCellStyle(i % 2 == 0 ? oddRowStyle : evenRowStyle);
            celda = filaAux.createCell(4);
            celda.setCellValue(((Cliente) (clientes.get(i))).sexo);
            celda.setCellStyle(i % 2 == 0 ? oddRowStyle : evenRowStyle);
            celda = filaAux.createCell(5);
            celda.setCellValue(((Cliente) (clientes.get(i))).descripcion);
            celda.setCellStyle(i % 2 == 0 ? oddRowStyle : evenRowStyle);
        }
        libro.write(file);
        file.close();
        return true;
    }

}
