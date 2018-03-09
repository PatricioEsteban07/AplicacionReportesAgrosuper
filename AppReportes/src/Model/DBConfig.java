/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que mantendrá información de la configuración de conexión a la base de datos.
 * @author Patricio
 */
public class DBConfig
{
    public String host;
    public String port;
    public String dbName;
    public String user;
    public String pass;
    private String fileDir = System.getProperty("user.home")+"/agrosuperConfig.txt";

    /**
     * Inicializa la clase, con parámetros ingresados por el usuario.
     */
    public DBConfig(String host, String port, String dbName, String user, String pass)
    {
        this.host = host;
        this.port = port;
        this.dbName = dbName;
        this.user = user;
        this.pass = pass;
    }

    /**
     * Inicializa la clase, pero con información por defecto, además de verificar que exista o no un archivo de 
     * configuración en el Sistema Operativo.
     */
    public DBConfig()
    {
        this.host="192.168.1.140";
        this.port="3306";
        this.dbName="db_gestionventas";
        this.user = "gestionVentas";
        this.pass = "gestionVentas";
        persistirInfoDB();
    }
    
    /**
     * Método que retorna una URL con el conector MySQL e información de la base de datos a conectar.
     */
    public String urlConector()
    {
        return "jdbc:mysql://"+this.host+":"+this.port+"/"+this.dbName;
    }

    /**
     * Método que verifica si existe un archivo de configuración de la base de datos en el Sistema Operativo: 
     * Si existe se extrae su información y se ingresa en la aplicación, si no existe se genera tal archivo con 
     * datos que la aplicación provee por defecto.
     */
    private void persistirInfoDB()
    {
        FileReader lector = null;
        try
        {
            File file = new File(fileDir);
            if(!file.exists())
            {
                FileWriter nuevo =null;
                try {
                    System.out.println("no existe");
                    nuevo = new FileWriter(fileDir);
                    nuevo.write(this.host+"\n");
                    nuevo.write(this.port+"\n");
                    nuevo.write(this.dbName+"\n");
                    nuevo.write(this.user+"\n");
                    nuevo.write(this.pass+"\n");
                    nuevo.close();
                    return;
                }
                catch (IOException ex) {
                    Logger.getLogger(DBConfig.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            System.out.println("existe");
            System.out.println("dir doc: "+fileDir);
            lector = new FileReader(fileDir);
            
            BufferedReader br = new BufferedReader(lector);
            this.host=br.readLine();
            this.port=br.readLine();
            this.dbName=br.readLine();
            this.user=br.readLine();
            this.pass=br.readLine();
            lector.close();
            return;
            
        }
        catch (IOException ex)
        {
            Logger.getLogger(DBConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para actualizar los datos de configuración de conexión a la base de datos. Se preocupa de actualizar los datos 
     * tanto en la aplicación como en el archivo que el Sistema Operativo almacena.
     * @return true cuando la operación es exitosa y false en caso contrario.
     */
    public boolean actualizarDatos(String host, String port, String name, String user, String pass)
    {
        BufferedReader br = null;
        try
        {
            this.host=host;
            this.port=port;
            this.dbName=name;
            this.user=user;
            this.pass=pass;   
            
            FileWriter nuevo = new FileWriter(fileDir);
            nuevo.write(this.host+"\n");
            nuevo.write(this.port+"\n");
            nuevo.write(this.dbName+"\n");
            nuevo.write(this.user+"\n");
            nuevo.write(this.pass+"\n");
            nuevo.close();
            
            return true;
        }
        catch (IOException ex)
        {
            Logger.getLogger(DBConfig.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
}
