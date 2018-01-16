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
public class Empresa extends Recurso
{
    public String nombre;
    public String direccion;
    public String descripcion;

    public Empresa(int id, String nombre, String direccion, String descripcion)
    {
        super(id);
        this.nombre = nombre;
        this.direccion = direccion;
        this.descripcion = descripcion;
    }
    
}
