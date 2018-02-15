/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB.CSVImport;

import Model.CommandNames;
import Model.LocalDB;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Patricio
 */
public abstract class CSVImport
{
    public final String dirBase=(System.getProperty("user.home")).replace("\\", "/")+"/Desktop/";
    public Connection conn;
    public LocalDB db;
    
    public CSVImport(LocalDB db)
    {
        this.db=db;
        this.conn=null;
        /*
        String loadQuery = "LOAD DATA LOCAL INFILE '"+this.dirBase+"CSV Base/zonaVentas.csv"+"' " 
            +"INTO TABLE zonaVentas FIELDS TERMINATED BY ';' ENCLOSED BY '\"' "
            +"LINES TERMINATED BY '\\n' IGNORE 8 ROWS";
        /*
        String loadQuery = "LOAD DATA LOCAL INFILE '"+this.dirBase+"CSV Base/oficinaVentas.csv"+"' " 
            +"INTO TABLE oficinaVentas FIELDS TERMINATED BY ';' ENCLOSED BY '\"' "
            +"LINES TERMINATED BY '\\n' IGNORE 8 ROWS";
        */
        /*
        String loadQuery = "LOAD DATA LOCAL INFILE '"+this.dirBase+"CSV Base/tipoCliente.csv"+"' " 
            +"INTO TABLE tipoCliente FIELDS TERMINATED BY ';' ENCLOSED BY '\"' "
            +"LINES TERMINATED BY '\\n' IGNORE 8 ROWS";
        */
        
        String loadQuery = "LOAD DATA LOCAL INFILE '"+this.dirBase+"CSV Base/clientes.csv"+"' " 
            +"INTO TABLE cliente FIELDS TERMINATED BY ';' ENCLOSED BY '\"' "
            +"LINES TERMINATED BY '\\n' IGNORE 8 ROWS";
        
        cargarCsv(loadQuery);
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
        int idxInicio=0;
        int idxFin=content.length();
        if(content.contains(caracter+""))
            return null;
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
            return content.substring(idxInicio, idxFin+1);
        }
        return null;
    }
            
            

    private void cargarCsv(String query)
    {
        try
        {
            this.connect();
            this.executeQuery(query);
            this.close();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public boolean connect() throws SQLException, ClassNotFoundException
    {
        if (conn == null)
        {
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                this.conn = DriverManager.getConnection(this.db.dbConfig.urlConector(), this.db.dbConfig.user, 
                        this.db.dbConfig.pass);
            }
            catch (SQLException ex)
            {
                System.out.println("connect - SQLException: " + ex);
                return false;
            }
            catch (ClassNotFoundException ex)
            {
                System.out.println("connect - ClassCastException: " + ex);
                return false;
            }
        }
        return true;
    }
    
    public ResultSet executeQuery(String query) throws SQLException
    {
        if (conn != null)
        {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);
            return result;
        }
        return null;
    }

    public void close() throws SQLException
    {
        if (this.conn != null)
            this.conn.close();
    }
}

