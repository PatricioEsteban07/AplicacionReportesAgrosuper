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
public class N3 extends Recurso
{
    public String nombre;
    public N2 n2;
    
    public N3(String id)
    {
        super(id);
    }

    public N3(String id, String nombre, N2 n2)
    {
        super(id);
        this.nombre=nombre;
        this.n2=n2;
    }
    
    
    
}
