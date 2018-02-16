/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.CommandNames;
import Model.LocalDB;
import Model.Recurso;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.scene.control.Alert;

/**
 *
 * @author Patricio
 */
public abstract class RecursoDB
{
    protected Connection conn = null;
    private String queryBase;
    protected String query;
    public String nombre;
    protected ArrayList<Recurso> datos;
    public LocalDB db;

    public RecursoDB(String nombre, String query, LocalDB db)
    {
        this.nombre=nombre;
        this.queryBase=query;
        this.query=query;
        this.datos = new ArrayList<>();
        this.db=db;
    }
    
    public RecursoDB(String nombre, String query, ArrayList<Recurso> datos, LocalDB db)
    {
        this.nombre=nombre;
        this.query=query;
        this.datos = datos;
        this.db=db;
    }
    
    public boolean connect() throws SQLException, ClassNotFoundException
    {
        if(conn!=null)
        {
            conn.close();
        }
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            this.conn = DriverManager.getConnection(this.db.dbConfig.urlConector(), this.db.dbConfig.user, 
                    this.db.dbConfig.pass);
        }
        catch (SQLException | ClassNotFoundException ex)
        {
                CommandNames.generaMensaje("Error de Sistema", Alert.AlertType.ERROR, "Error con procedimiento en Base de Datos", 
                    "Hubo problemas para ejecutar la consulta en la base de datos. El error es el siguiente: "+ex);
            System.out.println("connect - SQLException: " + ex);
            return false;
        }
        return true;
    }

    public ResultSet executeQuery() throws SQLException
    {
        if(conn!=null)
        {
            conn.close();
        }
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(this.query);
        return result;
    }

    public void close() throws SQLException
    {
        if (this.conn != null)
        {
            this.conn.close();
            this.conn=null;
        }
    }
    
    public ArrayList<Recurso> getAll()
    {
        return this.datos;
    }

    //IMPLEMENTAR DE ACUERDO A BUSQUEDA BINARIA
    public Recurso getById(String id)
    {
        if (this.datos == null || this.datos.isEmpty())
        {
            return null;
        }
        for (int i = 0; i < this.datos.size(); i++)
        {
            if(this.datos.get(i).id.equals(id))
            {
                return this.datos.get(i);
            }
        }
        return null;
    }

    public String getQueryBase()
    {
        return queryBase;
    }

    public String getQuery()
    {
        return query;
    }

    public void setQuery(String query)
    {
        this.query = query;
    }

    public int add(Recurso element)
    {
        this.datos.add(element);
        return this.datos.size();
    }
    
    public abstract boolean obtenerDatos();
}
