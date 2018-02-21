/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

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
        this.host="192.168.1.187";
        this.port="3306";
        this.dbName="db_ejemplo";
        this.user = "gestionVentas";
        this.pass = "gestionVentas";
    }
    
    public String urlConector()
    {
        return "jdbc:mysql://"+this.host+":"+this.port+"/"+this.dbName;
    }
    
    
    
}
