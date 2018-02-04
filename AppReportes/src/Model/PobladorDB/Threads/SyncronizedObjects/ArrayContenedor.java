/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB.Threads.SyncronizedObjects;

import Model.PobladorDB.Threads.PaqueteBuzon;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Patricio
 */
public class ArrayContenedor
{
    private HashMap<Integer,Row> contenedor;

    public ArrayContenedor()
    {
        this.contenedor = new HashMap<>();
    }
    
    public synchronized Row obtenerJob()
    {
        if(!this.contenedor.isEmpty())
        {
            for (Map.Entry<Integer, Row> entry : this.contenedor.entrySet()) {
                Row value = entry.getValue();
                this.contenedor.remove(entry.getKey());
                return value;
            }
        }
        return null;
    }
    
    public synchronized boolean enviarTarea(Integer key, Row row)
    {
        if(this.contenedor==null)
        {
            System.out.println("upsis null contenedor :c");
            return false;
        }
        this.contenedor.put(key, row);
        return true;
    }
    
    
    public synchronized boolean contenedorIsEmpty()
    {
        return this.contenedor.isEmpty();
    }
    
}
