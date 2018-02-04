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
public class Centro extends Recurso
{
    public String nombre;
    public Region region;
    
    public Centro(String id)
    {
        super(id);
    }

    public Centro(String id, String nombre, Region region)
    {
        super(id);
        this.nombre=nombre;
        this.region=region;
    }
    
    
    
}
