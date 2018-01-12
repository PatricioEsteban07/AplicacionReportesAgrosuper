/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import TablasReporte.Empresa_Cliente;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import sun.swing.plaf.synth.StyleAssociation;

/**
 *
 * @author Patricio
 */
public class DataExtract
{
    public Connection conn = null;
    
    public ArrayList<Cliente> clientes;
    public ArrayList<Empresa> empresas;
    public ArrayList<Empresa_Cliente> empresas_clientes;

    public DataExtract()
    {
        this.clientes = new ArrayList<>();
        this.empresas = new ArrayList<>();
        this.empresas_clientes = new ArrayList<>();
    }

    public boolean connect() throws SQLException, ClassNotFoundException
    {
        if (conn == null)
        {
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_app_reportes", "root", "12345678");
            }
            catch (SQLException ex)
            {
                System.out.println("connect - SQLException: "+ex);
                return false;
            }
            catch (ClassNotFoundException ex)
            {
                System.out.println("connect - ClassCastException: "+ex);
                return false;
            }
        }
        return true;
    }
    
    public void close() throws SQLException
    {
        if (this.conn != null)
        {
            this.conn.close();
        }
        this.conn=null;
    }

    public boolean ejecutarPeticion(String recurso, String query) throws SQLException, ClassNotFoundException
    {
        //conectar a DB
        connect();
        
        Statement stmt;
        ResultSet result=null;
        //obtener valores de DB
        if (conn != null)
        {
            try
            {
                stmt = conn.createStatement();
                result = stmt.executeQuery(query);
            }
            catch (SQLException e)
            {
                System.out.println("executeQuery - SQLException: "+e);
                return false;
            }
        }
        
        //recorrer result para crear objetos
        while(result!=null && result.next())
        {
            //distinción en objetos a crear
            switch(recurso){
                case CommandNames.clientes:
                    int idAux=result.getInt("id");
                    String nombreAux=result.getString("nombre");
                    String apellidoAux=result.getString("apellido");
                    int edadAux=result.getInt("edad");
                    String sexoAux=result.getString("sexo");
                    String descripcionAux=result.getString("descripcion");
                    this.clientes.add(new Cliente(idAux, nombreAux, apellidoAux, edadAux, sexoAux, descripcionAux));
                    break;
                case CommandNames.empresas:
                    idAux=result.getInt("id");
                    nombreAux=result.getString("nombre");
                    String direcionAux=result.getString("direccion");
                    descripcionAux=result.getString("descripcion");
                    this.empresas.add(new Empresa(idAux, nombreAux, direcionAux, descripcionAux));
                    break;
            }
        }
        close();
        return true;
    }
    
    public boolean ejecutarPeticionRelacion(String recurso, String query, ArrayList<String> relaciones) throws SQLException, ClassNotFoundException
    {
        //conectar a DB
        connect();
        
        Statement stmt;
        ResultSet result=null;
        //obtener valores de DB
        if (conn != null)
        {
            try
            {
                stmt = conn.createStatement();
                result = stmt.executeQuery(query);
            }
            catch (SQLException e)
            {
                System.out.println("executeQuery - SQLException: "+e);
                return false;
            }
        }
        
        //recorrer result para crear objetos
        while(result!=null && result.next())
        {
            //capturo valores fila (sólo relaciones)
            ArrayList<Integer> ids=new ArrayList<>();
            for (int i = 0; i < relaciones.size(); i++)
            {
                ids.add(result.getInt(relaciones.get(i)+"_id"));
            }
            
            //creo objeto que me maneje relaciones
            empresas_clientes.add(new Empresa_Cliente(ids));
            
            //creo linkeo entre objetos
            for (Empresa_Cliente empresas_cliente : this.empresas_clientes)
            {
                if(empresas_cliente.cliente==null || empresas_cliente.empresa==null)
                {
                    empresas_cliente.searchObjects(this.clientes,this.empresas);
                }
            }
            
        }
        close();
        return true;
    }
    
     
    private HSSFFont createFont(HSSFWorkbook workbook, short fontColor, short fontHeight, short fontBold) {
        HSSFFont font = workbook.createFont();
        font.setBoldweight(fontBold);
        font.setColor(fontColor);
        font.setFontName("Arial");
        font.setFontHeightInPoints(fontHeight);

        return font;
    }
    
    private HSSFCellStyle createStyle(HSSFWorkbook workbook, HSSFFont font, short cellAlign, short cellColor, boolean cellBorder, short cellBorderColor) 
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

    public boolean generarExcel(String nombreTabla, ArrayList<String> columns) throws FileNotFoundException, IOException
    {
       /*La ruta donde se creará el archivo*/
        String rutaArchivo = System.getProperty("user.home")+"/Desktop/"+nombreTabla+".xls";
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
        HSSFWorkbook libro = new HSSFWorkbook();
        /*Se inicializa el flujo de datos con el archivo xls*/
        FileOutputStream file = new FileOutputStream(archivoXLS);
        
        // Generate fonts
        HSSFFont headerFont  = createFont(libro,HSSFColor.WHITE.index, (short)12, Font.BOLDWEIGHT_BOLD);
        HSSFFont contentFont = createFont(libro,HSSFColor.BLACK.index, (short)10, Font.BOLDWEIGHT_NORMAL);

        // Generate styles
        HSSFCellStyle headerStyle  = createStyle(libro,headerFont,  HSSFCellStyle.ALIGN_CENTER, HSSFColor.BLUE_GREY.index,       true, HSSFColor.WHITE.index);
        HSSFCellStyle oddRowStyle  = createStyle(libro,contentFont, HSSFCellStyle.ALIGN_LEFT,   HSSFColor.WHITE.index, true, HSSFColor.GREY_80_PERCENT.index);
        HSSFCellStyle evenRowStyle = createStyle(libro,contentFont, HSSFCellStyle.ALIGN_LEFT,   HSSFColor.GREY_25_PERCENT.index, true, HSSFColor.GREY_80_PERCENT.index);
        
        /*Utilizamos la clase Sheet para crear una nueva hoja de trabajo dentro del libro que creamos anteriormente*/
        HSSFSheet hoja = libro.createSheet(nombreTabla);
                
        //inicialiar fila de nombres de columnas
        System.out.println("Creando nombres de columnas...");        
        HSSFRow fila = hoja.createRow( 0 );
        for (int i = 0; i < columns.size(); i++)
        {
            HSSFCell celda = fila.createCell(i);
            celda.setCellStyle(headerStyle);
            celda.setCellValue(columns.get(i));
            hoja.autoSizeColumn(i);
        }
        
        System.out.println("Rellenando tabla con valores...");
        /*Hacemos un ciclo para inicializar los valores de 10 filas de celdas*/
        
        switch(nombreTabla)
        {
            case CommandNames.clientes:
                for (int i = 0; this.clientes!=null && i<this.clientes.size(); i++)
                {
                    /*La clase Row nos permitirá crear las filas*/
                    HSSFRow filaAux = hoja.createRow(i+1);
                    
                    // Style depends on if row is odd or even

                    /*Cada fila tendrá 5 celdas de datos*/
                        /*Creamos la celda a partir de la fila actual*/
                    HSSFCell celda = filaAux.createCell(0);
                    celda.setCellValue(this.clientes.get(i).id);
                    celda.setCellStyle( i % 2 == 0 ? oddRowStyle : evenRowStyle );
                    celda = filaAux.createCell(1);
                    celda.setCellValue(this.clientes.get(i).nombre);
                    celda.setCellStyle( i % 2 == 0 ? oddRowStyle : evenRowStyle );
                    celda = filaAux.createCell(2);
                    celda.setCellValue(this.clientes.get(i).apellido);
                    celda.setCellStyle( i % 2 == 0 ? oddRowStyle : evenRowStyle );
                    celda = filaAux.createCell(3);
                    celda.setCellValue(this.clientes.get(i).edad);
                    celda.setCellStyle( i % 2 == 0 ? oddRowStyle : evenRowStyle );
                    celda = filaAux.createCell(4);
                    celda.setCellValue(this.clientes.get(i).sexo);
                    celda.setCellStyle( i % 2 == 0 ? oddRowStyle : evenRowStyle );
                    celda = filaAux.createCell(5);
                    celda.setCellValue(this.clientes.get(i).descripcion);
                    celda.setCellStyle( i % 2 == 0 ? oddRowStyle : evenRowStyle );
                }
                break;
            case CommandNames.empresas:
                for (int i = 0; this.empresas!=null && i<this.empresas.size(); i++)
                {
                    /*La clase Row nos permitirá crear las filas*/
                    HSSFRow filaAux = hoja.createRow(i+1);

                    /*Cada fila tendrá 5 celdas de datos*/
                    /*Creamos la celda a partir de la fila actual*/
                    HSSFCell celda = filaAux.createCell(0);
                    celda.setCellValue(this.empresas.get(i).id);
                    celda.setCellStyle( i % 2 == 0 ? oddRowStyle : evenRowStyle );
                    celda = filaAux.createCell(1);
                    celda.setCellValue(this.empresas.get(i).nombre);
                    celda.setCellStyle( i % 2 == 0 ? oddRowStyle : evenRowStyle );
                    celda = filaAux.createCell(2);
                    celda.setCellValue(this.empresas.get(i).direccion);
                    celda.setCellStyle( i % 2 == 0 ? oddRowStyle : evenRowStyle );
                    celda = filaAux.createCell(3);
                    celda.setCellValue(this.empresas.get(i).descripcion);
                    celda.setCellStyle( i % 2 == 0 ? oddRowStyle : evenRowStyle );
                }
                break;
        }
        
        /*Escribimos en el libro*/
        libro.write(file);
        /*Cerramos el flujo de datos*/
        file.close();
        
        return false;
    }


}
