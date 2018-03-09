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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Clase que mantendrá información de la configuración de conexión a la base de datos, además de métodos para la manipulación 
 * con la base de datos. Esta clase se instancia una sola vez en todo el sistema.
 * @author Patricio
 */
public class LocalDB
{
    public DBConfig dbConfig;
    
    public Connection conn = null;

    public LocalDB(DBConfig config)
    {
        this.dbConfig=config;
    }
    
    /**
     * Método que genera una conexión a la base de datos con información del objeto DBConfig. Tal conexión se almacena en la variable 
     * conn de esta misma clase para ser utilizada por quien la necesite.
     * @return true si la conexión se ha establecido, o false en caso contrario.
     * @exception SQLException
     * @exception ClassNotFoundException
     */
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

    /**
     * Método que ejecuta una consulta a la base de datos.
     * @param query contiene el String con la consulta a ejecutar.
     * @return ResultSet si la query ejecutada fué exitosa, o null en caso contrario.
     * @exception SQLException
     */
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
    
    /**
     * Método que ejecuta una consulta a la base de datos.
     * @param query contiene el String con la consulta a ejecutar.
     * @return un valor correspondiente al result(mayor a 0 es exitoso), o 0 en caso contrario.
     * @exception SQLException
     */
    public int executeQueryInt(String query) throws SQLException
    {

        if (conn != null)
        {
            Statement stmt = conn.createStatement();
            int result = stmt.executeUpdate(query);
            return result;
        }
        return 0;
    }

    /**
     * Método que cierra la conexión a la base de datos.
     * @exception SQLException
     */
    public void close() throws SQLException
    {
        if (this.conn != null)
        {
            this.conn.close();
            this.conn=null;
        }
    }
    
    /**
     * Método que realiza una prueba de conexión a la base de datos con información de configuración existente. 
     * Dependiendo de si la conexión fue exitosa o fallida, se desplegará un Alert informando del resultado.
     * @return true si la prueba de conexión fué exitosa, o false en caso contrario.
     */
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
