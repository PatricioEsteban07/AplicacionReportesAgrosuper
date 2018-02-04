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
public class Region extends Recurso
{
    public String nombre;
    public String pais;
    
    public Region(String id)
    {
        super(id);
    }

    public Region(String id, String nombre, String pais)
    {
        super(id);
        this.nombre = nombre;
        this.pais = pais;
    }
    
    
}
