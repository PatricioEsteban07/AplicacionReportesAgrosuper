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
public class Cliente
{
    public String id;
    public String nombre;
    public String apellido;
    public int edad;
    public char sexo;
    public String descripcion;

    public Cliente(String id, String nombre, String apellido, int edad, char sexo, String descripcion)
    {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.sexo = sexo;
        this.descripcion = descripcion;
    }
    
}
