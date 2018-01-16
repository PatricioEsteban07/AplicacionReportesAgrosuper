/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.Recurso;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Patricio
 */
public abstract class RecursoDB
{
    private Connection conn = null;
    private String query;
    public String nombre;
    protected ArrayList<Recurso> datos;

    public RecursoDB(String nombre, String query)
    {
        this.nombre=nombre;
        this.query=query;
        this.datos = new ArrayList<>();
    }
    
    public RecursoDB(String nombre, String query, ArrayList<Recurso> datos)
    {
        this.nombre=nombre;
        this.query=query;
        this.datos = datos;
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

    //OJO!, IMPLEMENTAR POBLADO DE INFO E DB A JAVA
    public ResultSet executeQuery() throws SQLException
    {
        if (conn != null)
        {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(this.query);
            return result;
        }
        return null;
    }

    public void close() throws SQLException
    {
        if (this.conn != null)
            this.conn.close();
    }
    
    public ArrayList<Recurso> getAll()
    {
        return this.datos;
    }

    //IMPLEMENTAR DE ACUERDO A BUSQUEDA BINARIA
    public Recurso getById(int id)
    {
        if (this.datos == null || this.datos.isEmpty())
        {
            return null;
        }
        for (int i = 0; i < this.datos.size(); i++)
        {
            if(this.datos.get(i).id==id)
            {
                return this.datos.get(i);
            }
        }
        return null;
    }

    public int add(Recurso element)
    {
        if (this.datos == null)
        {
            this.datos = new ArrayList<>();
        }
        if (this.datos.isEmpty())
        {
            this.datos.add(element);
            return 0;
        }
        int inicio = 0, fin = this.datos.size() - 1, actual = this.datos.size() / 2;
        boolean flag = true;
        while (flag)
        {
            if (fin - inicio > 1)
            {
                if (this.datos.get(actual).id < element.id)
                {
                    inicio = actual;
                }
                else if (this.datos.get(actual).id > element.id)
                {
                    fin = actual;
                }
                else
                {
                    //ojo! id duplicado, no se permite inserción
                    return -1;
                }
                actual = ((inicio + fin) / 2) + 1;
            }
            else//casos cuando fin==inicio y diferencia es igual a 1
            {
                if(this.datos.get(actual).id==element.id)
                {
                    //ojo! id duplicado, no se permite inserción
                    return -1;
                }
                if (this.datos.get(actual).id < element.id)
                {
                    actual++;
                }
                flag=false;
            }
        }
        if(actual==this.datos.size())
        {
            this.datos.add(element);
        }
        else
        {
            this.datos.add(actual, element);
        }
        return actual;
    }
    
    public abstract boolean obtenerDatos(HashMap<String,RecursoDB> resources);
}
