/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.CSVImport;

import Model.CommandNames;
import Model.LocalDB;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.control.Alert;

/**
 * Clase base para clases de tipo CSVImport, posee métodos que todos utilizan.
 * @author Patricio
 */
public class CSVImport
{
    public static final char separadorCSV = ';';
    public static final char contenedorCamposCSV = '\"';
    public Connection conn;
    public LocalDB db;
    
    public int cantRowsIgnoradas;
    public String fileName;
    public String fileDir;
    public String tableName;
    public ArrayList<String> types;
    
    /**
     * Constructor de la Clase
     * @param db contiene la instancia de LocalDB que el sistema maneja
     * @param rowsIgnore dependiendo de la instancia CSVImport, este valor varia, corresponde a las lineas a ignorar 
     * cuando se procesa el archivo CSV
     */
    public CSVImport(LocalDB db, int rowsIgnore)
    {
        this.db=db;
        this.conn=null;
        
        this.cantRowsIgnoradas=rowsIgnore;
        this.types=new ArrayList<>();
    }
    /**
     * Método que, dado un String, retorna su contenido eliminando elementos extras (").
     * @param content
     * @param caracter
     * @return el contenido encontrado o null en caso contrario.
     */
    public String obtenerContenido(String content, char caracter)
    {
        if(content==null || content.equals(""))
            return null;
        if(!content.contains("\""))
            return content;
        int idxInicio=0, idxFin=content.length();
        for (int i = 0; i < idxFin; i++)
        {
            if(content.charAt(i)==caracter)
            {
                idxInicio=i;
                break;
            }
        }
        for (int i = idxFin-1; i > idxInicio; i--)
        {
            if(content.charAt(i)==caracter)
            {
                idxFin=i;
                break;
            }
        }
        if(idxInicio!=idxFin)
        {
            return content.substring(idxInicio+1, idxFin);
        }
        return null;
    }
    
    /**
     * Método que se encarga de tomar un archivo CSV, procesarlo para crear otro con datos formateados y que se utiliza para 
     * finalmente cargarlo a la base de datos correspondiente. Este método realiza una validación simple respecto a la cantidad 
     * de columnas que debe tener el archivo CSV para la carga.
     * @return true si todos los pasos se realizaron exitosamente, y false si ocurrió algún error en el proceso.
     */
    public boolean procesarArchivo()
    {
        try
        {
            File inputFile = new File(this.fileDir);
            File tempFile = new File(System.getProperty("java.io.tmpdir") + "/tempCSV.csv");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            int ct_rows = 0;
            String currentLine;

            while ((currentLine = reader.readLine()) != null)
            {
                if (ct_rows >= this.cantRowsIgnoradas)
                {
                    String[] datos = currentLine.split(this.separadorCSV+"");
                    if(ct_rows == this.cantRowsIgnoradas && datos.length!=this.types.size())
                    {
                        CommandNames.generaMensaje("Error generando archivo", Alert.AlertType.ERROR,
                            "Problemas con columnas origen/destino", "Lo lamentamos, hubo un problema con la cantidad de columnas "
                            + "que posee el archivo CSV a cargar: no coincide con la cantidad de columnas que debería tener.");
                        return false;
                    }
                    if(!currentLine.equals(""))
                    {
                        String content="";
                        for (int i = 0; i < types.size(); i++)
                        {
                            datos[i] = obtenerContenido(datos[i], this.contenedorCamposCSV);
                            if(!datos[i].equals("#"))
                            {
                                switch (types.get(i))
                                {
                                    case "ID"://eliminar posiles 0 a la izquierda
                                        int j = 0;
                                        while (datos[i]!=null && j<datos[i].length() && datos[i].charAt(j) == '0')
                                        {
                                            j++;
                                        }
                                        datos[i] = datos[i].substring(j);
                                        break;
                                    case "INT"://hasta el momento procurar que sea int
                                        if(datos[i]==null || datos[i].equals(""))
                                            datos[i]="0";
                                        datos[i]=datos[i].replace(".", "");
                                        break;
                                    case "FLOAT"://formato 12.12
                                        datos[i]=datos[i].replace(".", "").replace(",", ".");
                                        break;
                                    case "FLOAT KG"://float de tabla facturaventas-material
                                        if(datos[i]==null || datos[i].equals(""))
                                            datos[i]="0";
                                        datos[i]=datos[i].replace(".", "").replace(",", ".").replace(" KG","");
                                        break;
                                    case "DATE"://formato 2018-01-30
                                        datos[i] = datos[i].substring(6) + "-" + datos[i].substring(3, 5) + "-" + datos[i].substring(0, 2);
                                        break;
                                    case "STRING"://mantener
                                        break;
                                }
                            }
                            content=content+this.contenedorCamposCSV+datos[i]+this.contenedorCamposCSV;
                            if(i<types.size()-1)
                                content=content+";";
                        }
                        writer.write(content+"\n");
                    }
                }
                ct_rows++;
            }
            writer.close();
            reader.close();
            tempFile.renameTo(inputFile);
            String dir = tempFile.getPath().replaceAll("\\\\", "/");
            return this.cargarCsv(dir, this.tableName);
        }
        catch (IOException ex)
        {
            CommandNames.generaMensaje("Error leyendo archivo", Alert.AlertType.ERROR,
                "Problemas con lectura de archivo CSV", "Lo lamentamos, hubo un problema al momento de realizar "
                        + "manipulación con el archivo CSV.");
            return false;
        }
    }
    
    /**
     * Método que se encarga de, en base a un archivo CSV, realizar la carga en la tabla respectiva. Dicha carga se realiza con 
     * un método de SQL: LOAD DATA.
     * @param fileDir contiene la dirección del archivo CSV a procesar.
     * @param tableName el nombre de la tabla de la base de datos a cargar.
     * @return true si la carga fué exitosa, o false en otro caso.
     */
    public boolean cargarCsv(String fileDir, String tableName)
    {
        String loadQuery="LOAD DATA LOCAL INFILE '"+fileDir+"' INTO TABLE "+tableName+" FIELDS TERMINATED BY '"
                +this.separadorCSV+"' ENCLOSED BY '"+this.contenedorCamposCSV+"' LINES TERMINATED BY '\\n';";
        try
        {
            this.connect();
            this.executeQuery(loadQuery);
            this.close();
            return true;
        }
        catch (ClassNotFoundException | SQLException ex)
        {
            System.out.println("OJO, PASO ALGO: "+ex);
            return false;
        }
    }
    
    /**
     * Método que se encarga de generar la conexión a la base de datos
     * @return true si la conexión se ha realizado, o false en caso contrario
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public boolean connect() throws SQLException, ClassNotFoundException
    {
        return this.db.connect();
    }
    
    /**
     * Método que ejecuta una consulta a la base de datos.
     * @param query contiene el String con la consulta a ejecutar.
     * @return un valor correspondiente al result(mayor a 0 es exitoso), o 0 en caso contrario.
     * @exception SQLException
     */
    public int executeQuery(String query) throws SQLException
    {
        return this.db.executeQueryInt(query);
    }

    /**
     * Método que cierra la conexión a la base de datos.
     * @throws SQLException
     */
    public void close() throws SQLException
    {
        this.db.close();
    }
    
}

