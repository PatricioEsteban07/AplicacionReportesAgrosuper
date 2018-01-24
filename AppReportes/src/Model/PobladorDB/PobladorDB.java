/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Patricio
 */
public abstract class PobladorDB
{
    
    private Connection conn = null;
    public final String dirBase=System.getProperty("user.home")+"/Desktop/";

    public PobladorDB()
    {

    }

    protected File openFile(String fileDir, String fileName)
    {
        File archivoExcel = new File(fileDir+fileName);
        if (!archivoExcel.exists())
        {
            System.out.println("Ojo, el archivo no existe :c");
            return null;
        }
        return archivoExcel;
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

    public boolean executeInsert(String verify, String query) throws SQLException
    {
        if (conn != null)
        {
            System.out.println("paso1");
            Statement stmt = conn.createStatement();
            System.out.println("paso2");
            if(!stmt.executeQuery(verify).next())
            {
            System.out.println("paso3");
                stmt.executeUpdate(query);
            System.out.println("paso3");
                return true;
            }
            System.out.println("paso4");
        }
        return false;
    }

    public void close() throws SQLException
    {
        if (this.conn != null)
            this.conn.close();
    }

}
