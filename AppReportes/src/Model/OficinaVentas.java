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
public class OficinaVentas extends Recurso
{
    public String id;
    public String nombre;
    public ZonaVentas zonaVentas;
    
    public OficinaVentas(String id)
    {
        super(id);
    }

    public OficinaVentas(String id, String nombre, ZonaVentas zonaVentas)
    {
        super(id);
        this.nombre = nombre;
        this.zonaVentas = zonaVentas;
    }
}
