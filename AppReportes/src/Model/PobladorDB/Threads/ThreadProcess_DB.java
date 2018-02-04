/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB.Threads;

import Model.CommandNames;
import Model.LocalDB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class ThreadProcess_DB extends ThreadProcess
{
    private Connection conn;
    private ArrayList<ThreadProcess> threads;
    
    public ThreadProcess_DB(int id, LocalDB db, Buzon buzon, ArrayList<ThreadProcess> threads)
    {
        super(id, db, buzon);
        this.threads=threads;
        this.flagExterno=true;
    }
    
    @Override
    public void run()
    {
        try
        {
            this.connect();
            while (flagExterno || !this.buzon.contenedorIsEmpty() || !this.buzon.buzonIsEmpty() 
                    || !threadsReady(threads))
            {
                PaqueteBuzon aux=this.buzon.obtenerPaquete();
                if(aux!=null)
                {
                    if(aux.id.containsKey("Centro") && this.executeInsert("SELECT id FROM centro WHERE id='"
                            +aux.id.get("Centro")+"'", aux.query.get("Centro")))
                        this.db.centros.add(aux.id.get("Centro"));
                    if(aux.id.containsKey("Oficina") && this.executeInsert("SELECT id FROM oficinaventas WHERE id='"
                            +aux.id.get("Oficina")+"'", aux.query.get("Oficina")))
                        this.db.oficinas.add(aux.id.get("Oficina"));
                    if(aux.id.containsKey("Material") && this.executeInsert("SELECT id FROM material WHERE id='"
                            +aux.id.get("Material")+"'", aux.query.get("Material")))
                        this.db.materiales.add(aux.id.get("Material"));
                    if(aux.id.containsKey("Pedido") && this.executeInsert("SELECT id FROM pedido WHERE id='"
                            +aux.id.get("Pedido")+"'", aux.query.get("Pedido")))
                        this.db.pedidos.add(aux.id.get("Pedido"));
                }
            }
            this.close();
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            Logger.getLogger(ThreadProcess_DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean threadsReady(ArrayList<ThreadProcess> threads)
    {
        for (ThreadProcess thread : threads)
        {
            if(thread.flagPropio)
                return false;
        }
        return true;
    }
    
    public boolean connect() throws SQLException, ClassNotFoundException
    {
        if (conn == null)
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

    public boolean executeInsert(String verify, String query) throws SQLException
    {
        if (conn != null)
        {
            Statement stmt = conn.createStatement();
            if(!stmt.executeQuery(verify).next())
            {
                stmt.executeUpdate(query);
                return true;
            }
        }
        return false;
    }

    public void close() throws SQLException
    {
        if (this.conn != null)
        {
            this.conn.close();
            this.conn=null;
        }
    }
}
