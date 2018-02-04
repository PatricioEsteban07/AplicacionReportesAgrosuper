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
public class Agrupado extends Recurso
{
    public String nombre;
    
    public Agrupado(String id)
    {
        super(id);
    }

    public Agrupado(String id, String nombre)
    {
        super(id);
        this.nombre = nombre;
    }
    
    
}
