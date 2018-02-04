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
public class N4 extends Recurso
{
    public String nombre;
    public N3 n3;
    
    public N4(String id)
    {
        super(id);
    }

    public N4(String id, String nombre, N3 n3)
    {
        super(id);
        this.nombre=nombre;
        this.n3=n3;
    }
    
    
    
}
