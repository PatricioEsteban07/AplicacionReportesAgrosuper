/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.GeneradoresExcel;

import Model.Cliente;
import Model.Empresa;
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
public class GeneradorExcel_ClienteEmpresas extends GeneradorExcel
{
    
    public GeneradorExcel_ClienteEmpresas()
    {
        super("Cliente-Empresas");
        this.columnas.add("cliente_id");
        this.columnas.add("cliente_nombre");
        this.columnas.add("cliente_apellido");
        this.columnas.add("cliente_edad");
        this.columnas.add("cliente_sexo");
        this.columnas.add("cliente_descripcion");
        this.columnas.add("empresa_id");
        this.columnas.add("empresa_nombre");
        this.columnas.add("empresa_direccion");
        this.columnas.add("empresa_descripcion");
    }

    @Override
    public boolean generarArchivo(HashMap<String,RecursoDB> resources) throws FileNotFoundException, IOException
    {
        //generación de archivo excel base
        String rutaArchivo = System.getProperty("user.home")+"/Desktop/"+nombreTabla+".xls";
        File archivoXLS = new File(rutaArchivo);
        
        //Se crea el libro de excel usando el objeto de tipo Workbook
        HSSFWorkbook libro = new HSSFWorkbook();
        //Se inicializa el flujo de datos con el archivo xls
        FileOutputStream file = new FileOutputStream(archivoXLS);
        
        // Generate fonts
        HSSFFont headerFont  = createFont(libro,HSSFColor.WHITE.index, (short)12, Font.BOLDWEIGHT_BOLD);
        HSSFFont contentFont = createFont(libro,HSSFColor.BLACK.index, (short)10, Font.BOLDWEIGHT_NORMAL);

        // Generate styles
        HSSFCellStyle headerStyle  = createStyle(libro,headerFont,  HSSFCellStyle.ALIGN_CENTER, HSSFColor.BLUE_GREY.index,       true, HSSFColor.WHITE.index);
        HSSFCellStyle oddRowStyle  = createStyle(libro,contentFont, HSSFCellStyle.ALIGN_LEFT,   HSSFColor.WHITE.index, true, HSSFColor.GREY_80_PERCENT.index);
        HSSFCellStyle evenRowStyle = createStyle(libro,contentFont, HSSFCellStyle.ALIGN_LEFT,   HSSFColor.GREY_25_PERCENT.index, true, HSSFColor.GREY_80_PERCENT.index);
        
        //Utilizamos la clase Sheet para crear una nueva hoja de trabajo dentro del libro que creamos anteriormente
        HSSFSheet hoja = libro.createSheet(nombreTabla);
                
        //inicialiar fila de nombres de columnas
        System.out.println("Creando nombres de columnas...");        
        HSSFRow fila = hoja.createRow( 0 );
        for (int i = 0; i < this.columnas.size(); i++)
        {
            HSSFCell celda = fila.createCell(i);
            celda.setCellStyle(headerStyle);
            celda.setCellValue(this.columnas.get(i));
            hoja.autoSizeColumn(i);
        }
        
        ArrayList<Recurso> empresas = resources.get("Empresas").getAll();
        
        System.out.println("Rellenando tabla con valores...");
        int contReg=1;
        for (Recurso empresa : empresas)
        {
            for (int i = 0; i < ((Empresa) empresa).clientes.size(); i++)
            {
                //La clase Row nos permitirá crear las filas
                HSSFRow filaAux = hoja.createRow(contReg);

                //Creamos la celda a partir de la fila actual
                HSSFCell celda = filaAux.createCell(0);
                celda.setCellValue(empresas.get(i).id);
                celda.setCellStyle( contReg % 2 == 0 ? oddRowStyle : evenRowStyle );
                celda = filaAux.createCell(1);
                celda.setCellValue(((Cliente)(((Empresa) empresa).clientes.get(i))).nombre);
                celda.setCellStyle( contReg % 2 == 0 ? oddRowStyle : evenRowStyle );
                celda = filaAux.createCell(2);
                celda.setCellValue(((Cliente)(((Empresa) empresa).clientes.get(i))).apellido);
                celda.setCellStyle( contReg % 2 == 0 ? oddRowStyle : evenRowStyle );
                celda = filaAux.createCell(3);
                celda.setCellValue(((Cliente)(((Empresa) empresa).clientes.get(i))).edad);
                celda.setCellStyle( contReg % 2 == 0 ? oddRowStyle : evenRowStyle );
                celda = filaAux.createCell(4);
                celda.setCellValue(((Cliente)(((Empresa) empresa).clientes.get(i))).sexo);
                celda.setCellStyle( contReg % 2 == 0 ? oddRowStyle : evenRowStyle );
                celda = filaAux.createCell(5);
                celda.setCellValue(((Cliente)(((Empresa) empresa).clientes.get(i))).descripcion);
                celda.setCellStyle( contReg % 2 == 0 ? oddRowStyle : evenRowStyle );
                celda = filaAux.createCell(6);
                celda.setCellValue(((Empresa) empresa).id);
                celda.setCellStyle( contReg % 2 == 0 ? oddRowStyle : evenRowStyle );
                celda = filaAux.createCell(7);
                celda.setCellValue(((Empresa) empresa).nombre);
                celda.setCellStyle( contReg % 2 == 0 ? oddRowStyle : evenRowStyle );
                celda = filaAux.createCell(8);
                celda.setCellValue(((Empresa) empresa).direccion);
                celda.setCellStyle( contReg % 2 == 0 ? oddRowStyle : evenRowStyle );
                celda = filaAux.createCell(9);
                celda.setCellValue(((Empresa) empresa).descripcion);
                celda.setCellStyle( contReg % 2 == 0 ? oddRowStyle : evenRowStyle );
                contReg++;
            }
        }
        System.out.println("Registros CE: "+(contReg-1));
        libro.write(file);
        file.close();
        return true;
    }

    
}