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
public class Cliente extends Recurso
{
    public String nombre;
    public String apellido;
    public int edad;
    public String sexo;
    public String descripcion;

    public Cliente(String id, String nombre, String apellido, int edad, String sexo, String descripcion)
    {
        super(id);
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.sexo = sexo;
        this.descripcion = descripcion;
    }
    
}
