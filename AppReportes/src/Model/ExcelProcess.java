/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/*Librerías para trabajar con archivos excel*/
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
/**
 *
 * @author Patricio
 */
public class ExcelProcess
{    
    public boolean generarArchivo(String fileName, String sheetName, ResultSet results, ArrayList<String> columns) throws IOException, SQLException
    {
       /*La ruta donde se creará el archivo*/
        String rutaArchivo = System.getProperty("user.home")+"/Desktop/"+fileName+".xls";
        /*Se crea el objeto de tipo File con la ruta del archivo*/
        File archivoXLS = new File(rutaArchivo);
        
        //----------------------------------------------------------
        //OJO EN ESTA PARTE, A FUTURO REUTILIZAR ARCHIVOS GENERADOS
        /*Si el archivo existe se elimina*/
        if(archivoXLS.exists()) archivoXLS.delete();
        /*Se crea el archivo*/
        archivoXLS.createNewFile();
        //----------------------------------------------------------
        
        
        
        
        /*Se crea el libro de excel usando el objeto de tipo Workbook*/
        Workbook libro = new HSSFWorkbook();
        /*Se inicializa el flujo de datos con el archivo xls*/
        FileOutputStream file = new FileOutputStream(archivoXLS);
        
        /*Utilizamos la clase Sheet para crear una nueva hoja de trabajo dentro del libro que creamos anteriormente*/
        Sheet hoja = libro.createSheet(sheetName);
        
        //inicialiar fila de nombres de columnas
        System.out.println("Creando nombres de columnas...");
        Row fila = hoja.createRow(0);
        for (int i = 0; i < columns.size(); i++)
        {
            Cell celda = fila.createCell(i);
            celda.setCellValue(columns.get(i));
        }
  
        
        //aprox 2 millones, excel soporta aprox 1.048.576 filas y 16.384 columnas
        
        
        /*
        columns.add("cliente_id");
        columns.add("cliente_nombre");
        columns.add("cliente_apellido");
        columns.add("cliente_edad");
        columns.add("cliente_sexo");
        columns.add("cliente_descripcion");
        columns.add("empresa_id");
        columns.add("empresa_nombre");
        columns.add("empresa_direccion");
        columns.add("empresa_descripcion");
        */
        
        
        System.out.println("Rellenando tabla con valores...");
        /*Hacemos un ciclo para inicializar los valores de 10 filas de celdas*/
        for(int numFila=1;results!=null && results.next();numFila++){
            /*La clase Row nos permitirá crear las filas*/
            Row filaAux = hoja.createRow(numFila);
            
                System.out.println("pase1");
            /*Cada fila tendrá 5 celdas de datos*/
            for(int c=0;c<columns.size();c++){
                /*Creamos la celda a partir de la fila actual*/
                Cell celda = filaAux.createCell(c);
                System.out.println("pase2");
                celda.setCellValue(results.getString(columns.get(c)));
                System.out.println("fila "+numFila+" / col "+c);
            }
        }
        /*Escribimos en el libro*/
        libro.write(file);
        /*Cerramos el flujo de datos*/
        file.close();
        
        
        return false;
    }
}
