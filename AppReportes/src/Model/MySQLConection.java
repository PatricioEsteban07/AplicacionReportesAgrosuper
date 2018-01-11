/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Patricio
 */
public class MySQLConection
{
    public Connection conn = null;
    public ResultSet result = null;

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

    public boolean executeQuery(String query) throws SQLException
    {
        if (conn != null)
        {
            try
            (Statement stmt = conn.createStatement()) {
                this.result = stmt.executeQuery(query);
                return true;
            }
            catch (SQLException e)
            {
                System.out.println("executeQuery - SQLException: "+e);
                return false;
            }
        }
        return false;
    }

    public void close() throws SQLException
    {
        if (this.conn != null)
        {
            this.conn.close();
        }
    }
}