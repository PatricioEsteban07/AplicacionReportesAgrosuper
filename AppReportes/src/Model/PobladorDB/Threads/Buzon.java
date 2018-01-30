/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB.Threads;

import java.util.HashMap;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Patricio
 */
public class Buzon
{
    private HashMap<Integer,Row> contenedor;
    private HashMap<String,PaqueteBuzon> buzon;

    public Buzon()
    {
        this.contenedor=new HashMap<>();
        this.buzon=new HashMap<>();
    }

    public synchronized Row obtenerJob()
    {
        if(this.contenedor.isEmpty())
            return null;
        //implementar para obtener tarea del contenedor
        return null;
    }
    
    public synchronized boolean enviarTarea(Integer key, Row row)
    {
        if(this.contenedor==null)
            return false;
        this.contenedor.put(key, row);
        return true;
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
        if(this.buzon.isEmpty())
            return null;
        //implementar para obtener paquete del contenedor
        return null;
    }
    
    public synchronized boolean buzonIsEmpty()
    {
        return this.buzon.isEmpty();
    }
    
    public synchronized boolean contenedorIsEmpty()
    {
        return this.contenedor.isEmpty();
    }
}
