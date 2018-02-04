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
public class N2 extends Recurso
{
    public String nombre;
    public Sector sector;
    
    public N2(String id)
    {
        super(id);
    }

    public N2(String id, String nombre, Sector sector)
    {
        super(id);
        this.nombre=nombre;
        this.sector=sector;
    }
    
    
    
}
