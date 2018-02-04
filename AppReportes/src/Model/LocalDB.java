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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Patricio
 */
public class LocalDB
{
    public HashMap<String, Material> materiales;
    public HashMap<String, Marca> marcas;
    public HashMap<String, Sector> sectores;
    public HashMap<String, Agrupado> agrupados;
    public HashMap<String, TipoEnvasado> tiposEnvasados;
    public HashMap<String, EstadoRefrigerado> estadosRefrigerados;
    public HashMap<String, N2> n2s;
    public HashMap<String, N3> n3s;
    public HashMap<String, N4> n4s;
    public HashMap<String, Centro> centros;
    public HashMap<String, OficinaVentas> oficinas;
    public HashMap<String, Pedido> pedidos;
    public HashMap<String, Stock> stocks;
    public HashMap<String, Despacho> despachos;
    public HashMap<String, Cliente> clientes;
    public HashMap<String, Region> regiones;
    public HashMap<String, ZonaVentas> zonaVentas;
    public HashMap<String, TipoCliente> tiposClientes;
    public HashMap<String, ClienteLocal> clientesLocales;
    
    private Connection conn = null;

    public LocalDB() throws SQLException
    {
        this.materiales = new HashMap<>();
        this.marcas = new HashMap<>();
        this.sectores = new HashMap<>();
        this.agrupados = new HashMap<>();
        this.tiposEnvasados = new HashMap<>();
        this.estadosRefrigerados = new HashMap<>();
        this.n2s = new HashMap<>();
        this.n3s = new HashMap<>();
        this.n4s = new HashMap<>();
        this.centros = new HashMap<>();
        this.oficinas = new HashMap<>();
        this.pedidos = new HashMap<>();
        this.stocks = new HashMap<>();
        this.despachos = new HashMap<>();
        this.clientes = new HashMap<>();
        this.regiones = new HashMap<>();
        this.zonaVentas = new HashMap<>();
        this.tiposClientes = new HashMap<>();
        this.clientesLocales = new HashMap<>();
        /*
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
        }*/
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
