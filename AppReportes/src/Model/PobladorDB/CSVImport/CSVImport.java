/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB.CSVImport;

import Model.CommandNames;
import Model.LocalDB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Patricio
 */
public class CSVImport
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
                this.conn = DriverManager.getConnection(CommandNames.URL_CONNECT_DB, "root", "12345678");
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

