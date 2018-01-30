/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB.Threads;

/**
 *
 * @author Patricio
 */
public class PaqueteBuzon
{
    public String tipo;
    public String id;
    public String query;

    public PaqueteBuzon(String tipo, String id, String query)
    {
        this.tipo = tipo;
        this.id = id;
        this.query = query;
    }
    
    
}
