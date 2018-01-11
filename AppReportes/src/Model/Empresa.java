/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.HashMap;

/**
 *
 * @author Patricio
 */
public class Empresa
{
    public String id;
    public String nombre;
    public String direccion;
    public String descripcion;
    public HashMap<String,Cliente> clientes;

    public Empresa(String id, String nombre, String direccion, String descripcion)
    {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.descripcion = descripcion;
        this.clientes=new HashMap<>();
    }
    
}
