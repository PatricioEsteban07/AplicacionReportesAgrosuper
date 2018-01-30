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
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class LocalDB
{
    public Set<String> materiales;
    public Set<String> marcas;
    public Set<String> sectores;
    public Set<String> agrupados;
    public Set<String> tiposEnvasados;
    public Set<String> estadosRefrigerados;
    public Set<String> n2s;
    public Set<String> n3s;
    public Set<String> n4s;
    public Set<String> centros;
    public Set<String> oficinas;
    public Set<String> pedidos;
    public Set<String> stocks;
    public Set<String> despachos;
    public Set<String> clientes;
    
    private Connection conn = null;

    public LocalDB() throws SQLException
    {
        this.materiales = new HashSet<String>();
        this.marcas = new HashSet<String>();
        this.sectores = new HashSet<String>();
        this.agrupados = new HashSet<String>();
        this.tiposEnvasados = new HashSet<String>();
        this.estadosRefrigerados = new HashSet<String>();
        this.n2s = new HashSet<String>();
        this.n3s = new HashSet<String>();
        this.n4s = new HashSet<String>();
        this.centros = new HashSet<String>();
        this.oficinas = new HashSet<String>();
        this.pedidos = new HashSet<String>();
        this.stocks = new HashSet<String>();
        this.despachos = new HashSet<String>();
        this.clientes = new HashSet<String>();
        
        try
        {
            consultarExistencias(this.materiales,"SELECT id FROM material");
            consultarExistencias(this.marcas,"SELECT id FROM marca");
            consultarExistencias(this.sectores,"SELECT id FROM sector");
            consultarExistencias(this.agrupados,"SELECT id FROM agrupado");
            consultarExistencias(this.tiposEnvasados,"SELECT id FROM tipoenvasado");
            consultarExistencias(this.estadosRefrigerados,"SELECT id FROM estadorefrigerado");
            consultarExistencias(this.n2s,"SELECT id FROM n2");
            consultarExistencias(this.n3s,"SELECT id FROM n3");
            consultarExistencias(this.n4s,"SELECT id FROM n4");
            consultarExistencias(this.centros,"SELECT id FROM centro");
            consultarExistencias(this.oficinas,"SELECT id FROM oficinaventas");
       //     consultarExistencias(this.pedidos,"SELECT id FROM pedido");
       //     consultarExistencias(this.stocks,"SELECT id FROM stock");
       //     consultarExistencias(this.despachos,"SELECT id FROM despacho");
            consultarExistencias(this.clientes,"SELECT id FROM cliente");
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(LocalDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean consultarExistencias(Set<String> dbLocal, String query) throws SQLException, ClassNotFoundException
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(CommandNames.URL_CONNECT_DB, "root", "12345678");
        }
        catch (ClassNotFoundException | SQLException ex)
        {
            System.out.println("Problemas para consultar existentes: " + ex);
            close();
            return false;
        }
        connect();
        ResultSet result = executeQuery(query);
        while (result != null && result.next())
        {
            dbLocal.add(result.getString("id"));
        }
        close();
        return true;
    }
    
    public boolean connect() throws SQLException, ClassNotFoundException
    {
        if (this.conn == null)
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
        {
            this.conn.close();
        }
    }
    
}
