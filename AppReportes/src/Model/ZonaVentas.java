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
public class ZonaVentas extends Recurso
{
    public String id;
    public String nombre;
    
    public ZonaVentas(String id)
    {
        super(id);
    }

    public ZonaVentas(String id, String nombre)
    {
        super(id);
        this.nombre = nombre;
    }
}
