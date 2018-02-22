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
import java.util.Set;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Patricio
 */
public class LocalDB
{
    public DBConfig dbConfig;
    
    private Connection conn = null;

    public LocalDB(DBConfig config)
    {
        this.dbConfig=config;
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
        Alert alertAux=CommandNames.generaMensaje("Conectando a la Base de datos", 
                Alert.AlertType.NONE, null,"Estamos intentando una conexión a la base de datos de acuerdo a la información ingresada...",false);
        alertAux.show();
        try
        {
            if(!connect())
            {
                CommandNames.generaMensaje("Problemas de Configuración", Alert.AlertType.ERROR, "Error de aplicación",
                    "Reconfigure información de base de datos, información actual no puede ser utilizada para la conexión.");
                alertAux.getDialogPane().getButtonTypes().add(ButtonType.OK);
                alertAux.close();
                return false;
            }
            close();
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            CommandNames.generaMensaje("Problemas de Configuración", Alert.AlertType.ERROR, "Error de Sistema",
                "Hubo un problema con la DB, información actual no puede ser utilizada para la conexión.");
            alertAux.getDialogPane().getButtonTypes().add(ButtonType.OK);
            alertAux.close();
            return false;
        }
        CommandNames.generaMensaje("Información del Sistema", Alert.AlertType.INFORMATION, "Información del sistema", 
            "Conexión a la base de datos realizada exitosamente. Puede generar reportes siempre y cuando la información exista.");
        alertAux.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alertAux.close();
        return true;
    }
    
}
