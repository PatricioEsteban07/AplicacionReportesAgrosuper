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
public class Canal extends Recurso
{
    public String id;
    public String nombre;
    
    public Canal(String id)
    {
        super(id);
    }

    public Canal(String id, String nombre)
    {
        super(id);
        this.nombre = nombre;
    }
}
