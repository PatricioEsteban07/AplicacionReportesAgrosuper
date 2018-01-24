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
public class Material extends Recurso
{
    public String nombre;

    public Material(int id, String nombre)
    {
        super(id);
        this.nombre=nombre;
    }
    
}
