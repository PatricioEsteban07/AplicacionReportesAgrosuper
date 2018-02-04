/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB.Threads.SyncronizedObjects;

import Model.PobladorDB.Threads.PaqueteBuzon;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Patricio
 */
public class ArrayBuzon
{
    private HashMap<String,PaqueteBuzon> buzon;

    public ArrayBuzon()
    {
        this.buzon = new HashMap<>();
    }
    
    public synchronized boolean enviarPaquete(String key, PaqueteBuzon paqueteBuzon)
    {
        if(this.buzon.containsKey(key))
            return false;
        this.buzon.put(key, paqueteBuzon);
        return true;
    }
    
    public synchronized PaqueteBuzon obtenerPaquete()
    {
        if(!this.buzon.isEmpty())
        {
            for (Map.Entry<String, PaqueteBuzon> entry : this.buzon.entrySet()) {
                PaqueteBuzon value = entry.getValue();
                this.buzon.remove(entry.getKey());
                return value;
            }    
        }
        return null;
    }
    
    public synchronized boolean buzonIsEmpty()
    {
        return this.buzon.isEmpty();
    }
    
}
