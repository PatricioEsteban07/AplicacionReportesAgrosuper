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
import java.io.FileNotFoundException;
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
public abstract class CSVImport
{
    public final char separadorCSV = ';';
    public final char contenedorCamposCSV = '\"';
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
    
    public boolean validarName(String fileNameBase, String fileNameConsulta)
    {
        return fileNameBase.equals(fileNameConsulta);
    }
    
    public boolean formatCSV(String fileDir, ArrayList<String> types)
    {
        String separadorCSV = ";";
        BufferedReader br = null;
        String line = "";
        try {
            br = new BufferedReader(new FileReader(fileDir));
            while ((line = br.readLine()) != null) {                
                String[] datos = line.split(separadorCSV);
                //Imprime datos.
                for (int i = 0; i < types.size(); i++)
                {
                    //datos[i]
                    datos[i]=obtenerContenido(datos[i], '"');
                    switch(types.get(i))
                    {
                        case "ID"://eliminar posiles 0 a la izquierda
                            int j=0;
                            while(datos[i].charAt(j)=='0'){j++;}
                            datos[i]=datos[i].substring(j);
                            break;
                        case "INT"://hasta el momento procurar que sea int
                            
                            break;
                        case "FLOAT"://formato 12.12
                            datos[i].replace(".","").replace(",", ".");
                            break;
                        case "DATE"://formato 2018-01-30
                            datos[i]=datos[i].substring(6)+"-"+datos[i].substring(3,5)+"-"+datos[i].substring(0,2);
                            break;
                        case "STRING"://mantener
                            
                            break;
                    }
                    System.out.print(datos[i]+"/");
                }
               System.out.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //cargar archivo csv para procesar
            //si no existe archivo, retornar false con msj 
            //si no se puede abrir archivo, retornar false con msj
        //abrir archivo
        //leer linea por linea
        //no pescar lineas de comentarios (tratar)
        //comprobar que cant types correspondan a numero de columnas
            //si no corresponden retornar frase con msj
        //para cada linea
            //capturar elementos por separado (ArrayList<String>)
            //para cada elemento del araylist
                //definir tipo esperado de acuerdo a types
                    //si es Date, formatear omo corresponde
                    //si es String, dejar igual
                    //si es ID eliminar 0 a la izq
                    //si es float dejar con estructura algo.otro
                //reescribir elemento en doc / cargar directo en DB
                    //si se carga directo en la db, comprobar si es nuevo/existente
                    //en caso que no se pueda, retornar false con msj
        
        return false;
    }
    
    public String obtenerContenido(String content, char caracter)
    {
        if(content==null || content.equals(""))
            return null;
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
            File inputFile = new File(this.fileDir + "/" + this.fileName + ".csv");
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
                    //Imprime datos.
                    if(!currentLine.equals(""))
                    {
                        String content="";
                        for (int i = 0; i < types.size(); i++)
                        {
                            //datos[i]
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
                                        break;
                                    case "FLOAT"://formato 12.12
                                        datos[i].replace(".", "").replace(",", ".");
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
            System.out.println("R: "+res);
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

