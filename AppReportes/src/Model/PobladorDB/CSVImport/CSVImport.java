/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB.CSVImport;

import Model.CommandNames;
import Model.LocalDB;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

/**
 *
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
    
    public CSVImport(LocalDB db, int rowsIgnore)
    {
        this.db=db;
        this.conn=null;
        
        this.cantRowsIgnoradas=rowsIgnore;
        this.types=new ArrayList<>();
    }
    
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
             //   System.out.println("--------------------");
             //   System.out.println("D: "+currentLine);
                if (ct_rows >= this.cantRowsIgnoradas)
                {
                    String[] datos = currentLine.split(this.separadorCSV+"");
                    //Imprime datos.
                    if(ct_rows == this.cantRowsIgnoradas && datos.length!=this.types.size())
                    {
                        System.out.println("OJO: CANT ELEMENTOS CSV NO COINCIDE CON CANT COLUMNAS TABLA! NO SE AGREGARÁN");
                        System.out.println("datos: "+datos.length+" /col: "+this.types.size());
                        return false;
                    }
                    if(!currentLine.equals(""))
                    {
                        String content="";
                        for (int i = 0; i < types.size(); i++)
                        {
                            //datos[i]
                            datos[i] = obtenerContenido(datos[i], this.contenedorCamposCSV);
                            
                      //      System.out.println("->"+datos[i]);
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
                        //System.out.println("D: "+content);
                        writer.write(content+"\n");
                    }
                }
                ct_rows++;
            }
            System.out.println("rows: "+ct_rows);
            writer.close();
            reader.close();
            tempFile.renameTo(inputFile);
            String dir = tempFile.getPath().replaceAll("\\\\", "/");
            return this.cargarCsv(dir, this.tableName);
      //      GeneradorExcel_ReporteDisponibilidad.copyFile(tempFile, new File(System.getProperty("user.home") + "/Desktop/"+fileName+".csv"));   
        }
        catch (IOException ex)
        {
            System.out.println("Problemas para leer archivo!");
            Logger.getLogger(CSVImport_Region.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
       
    public boolean cargarCsv(String fileDir, String tableName)
    {
        String loadQuery="LOAD DATA LOCAL INFILE '"+fileDir+"' INTO TABLE "+tableName+" FIELDS TERMINATED BY '"
                +this.separadorCSV+"' ENCLOSED BY '"+this.contenedorCamposCSV+"' LINES TERMINATED BY '\\n';";
        System.out.println("L: "+loadQuery);
        try
        {
            this.connect();
            int res = this.executeQuery(loadQuery);
            this.close();
            System.out.println("Result: "+res);
            System.out.println("-----------------");
            return true;
        }
        catch (ClassNotFoundException | SQLException ex)
        {
            System.out.println("OJO, PASO ALGO: "+ex);
            return false;
        }
    }
    
    public boolean connect() throws SQLException, ClassNotFoundException
    {
        if (conn != null)
        {
            this.conn.close();
            this.conn=null;
        }
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            this.conn = DriverManager.getConnection(this.db.dbConfig.urlConector(), this.db.dbConfig.user, 
                    this.db.dbConfig.pass);
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            CommandNames.generaMensaje("Error del Sistema", Alert.AlertType.ERROR, "Error conexión DB", 
                    "Hubo un problema al momento de conectar a la base de datos. El error es el siguiente: "+ex);
            return false;
        }
        return true;
    }
    
    public int executeQuery(String query) throws SQLException
    {
        if (conn != null)
        {
            Statement stmt = conn.createStatement();
            int result = stmt.executeUpdate(query);
            return result;
        }
        return 0;
    }

    public void close() throws SQLException
    {
        if (this.conn != null)
            this.conn.close();
        this.conn=null;
    }
    
}

