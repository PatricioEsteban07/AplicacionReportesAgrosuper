/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB;

import Model.CommandNames;
import Model.LocalDB;
import Model.PobladorDB.Threads.Buzon;
import com.monitorjbl.xlsx.StreamingReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Patricio
 */
public abstract class PobladorDB
{
    public Buzon buzon;
    private Connection conn = null;
    public final String dirBase=System.getProperty("user.home")+"/Desktop/";
    public LocalDB db;

    public PobladorDB(LocalDB db)
    {
        this.db=db;
        this.buzon = new Buzon();
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

    public boolean executeInsert(String verify, String query) throws SQLException
    {
        if (conn != null)
        {
            Statement stmt = conn.createStatement();
            if(!stmt.executeQuery(verify).next())
            {
                stmt.executeUpdate(query);
                return true;
            }
        }
        return false;
    }

    public void close() throws SQLException
    {
        if (this.conn != null)
        {
            this.conn.close();
            this.conn=null;
        }
    }

    public boolean validarContenido(String value)
    {
        switch(value)
        {
            case "(en blanco)":
                return false;
            case "":
                return false;
        }
        return true;
    }

    protected String fomatearMes(String month)
    {
        switch(month)
        {
            case "ene":
                return "01";
            case "feb":
                return "02";
            case "mar":
                return "03";
            case "abr":
                return "04";
            case "may":
                return "05";
            case "jun":
                return "06";
            case "jul":
                return "07";
            case "ago":
                return "08";
            case "sep":
                return "09";
            case "oct":
                return "10";
            case "nov":
                return "11";
            case "dic":
                return "12";
        }
        return null;
    }

}
