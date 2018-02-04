/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB.Threads;

import java.util.HashMap;

/**
 *
 * @author Patricio
 */
public class PaqueteBuzon
{
    public HashMap<String,String> id;
    public HashMap<String,String> query;

    public PaqueteBuzon()
    {
        this.id=new HashMap<>();
        this.query=new HashMap<>();
    }
    
    public void addContenido(String tipo, String id, String query)
    {
        this.id.put(tipo,id);
        this.query.put(tipo,query);
    }
    
}
