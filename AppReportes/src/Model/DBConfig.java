/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
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

    public DBConfig(String host, String port, String dbName, String user, String pass)
    {
        this.host = host;
        this.port = port;
        this.dbName = dbName;
        this.user = user;
        this.pass = pass;
    }

    public DBConfig()
    {
        this.host="localhost";
        this.port="3306";
        this.dbName="db_ejemplo2";
        this.user = "gestionVentas";
        this.pass = "gestionVentas";
        persistirInfoDB();
    }
    
    public String urlConector()
    {
        return "jdbc:mysql://"+this.host+":"+this.port+"/"+this.dbName;
    }

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
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(DBConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(DBConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(DBConfig.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        catch (IOException ex)
        {
            Logger.getLogger(DBConfig.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
}
