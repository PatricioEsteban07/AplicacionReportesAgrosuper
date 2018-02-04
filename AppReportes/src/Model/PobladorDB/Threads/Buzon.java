/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB.Threads;

import Model.PobladorDB.Threads.SyncronizedObjects.ArrayBuzon;
import Model.PobladorDB.Threads.SyncronizedObjects.ArrayContenedor;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Patricio
 */
public class Buzon
{
    public ArrayContenedor contenedor;
    public ArrayBuzon buzon;

    public Buzon()
    {
        this.contenedor=new ArrayContenedor();
        this.buzon=new ArrayBuzon();
    }
    
    public boolean enviarPaquete(String key, PaqueteBuzon paqueteBuzon)
    {
        return this.buzon.enviarPaquete(key, paqueteBuzon);
    }
    
    public PaqueteBuzon obtenerPaquete()
    {
        return this.buzon.obtenerPaquete();
    }
    
    public boolean buzonIsEmpty()
    {
        return this.buzon.buzonIsEmpty();
    }
    
    public Row obtenerJob()
    {
        return this.contenedor.obtenerJob();
    }
    
    public boolean enviarTarea(Integer key, Row row)
    {
        return this.contenedor.enviarTarea(key, row);
    }
    
    
    public boolean contenedorIsEmpty()
    {
        return this.contenedor.contenedorIsEmpty();
    }
}
