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
import java.util.Set;
import javafx.scene.control.Alert;

/**
 *
 * @author Patricio
 */
public class LocalDB
{
    public DBConfig dbConfig;
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

    public LocalDB(DBConfig config)
    {
        this.dbConfig=config;
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
    }
    
    private boolean consultarExistencias(Set<String> dbLocal, String query) throws SQLException, ClassNotFoundException
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbConfig.urlConector(), dbConfig.user, dbConfig.pass);
        }
        catch (ClassNotFoundException | SQLException ex)
        {
            CommandNames.generaMensaje("Aviso de Reporte", Alert.AlertType.ERROR, "Problema al ejecutar SP en DB",
                    "Hubo un problema al momento de ejecutar la consulta en la Base de Datos. El error es el siguiente: "+ex);
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
                this.conn = DriverManager.getConnection(dbConfig.urlConector(), dbConfig.user, dbConfig.pass);
            }
            catch (SQLException | ClassNotFoundException ex)
            {
                CommandNames.generaMensaje("Aviso de Reporte", Alert.AlertType.ERROR, "Problema al conectar con DB",
                    "Hubo un problema al momento de conectarse a la base de datos. El error es el siguiente: "+ex);
                System.out.println("connect - SQLException: " + ex);
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
            this.conn=null;
        }
    }
    
    public boolean probarDBConection()
    {
        try
        {
            if(!connect())
            {
                CommandNames.generaMensaje("Problemas de Configuración", Alert.AlertType.ERROR, "Error de aplicación",
                    "Reconfigure información de base de datos, información actual no puede ser utilizada para la conexión.");
                return false;
            }
            close();
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            CommandNames.generaMensaje("Problemas de Configuración", Alert.AlertType.ERROR, "Error de Sistema",
                "Hubo un problema con la DB, información actual no puede ser utilizada para la conexión.");
            return false;
        }
        CommandNames.generaMensaje("Información del Sistema", Alert.AlertType.INFORMATION, "Información del sistema", 
            "Conexión a la base de datos realizada exitosamente. Puede generar reportes siempre y cuando la información exista."); 
        return true;
    }
    
}
