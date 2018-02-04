/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Date;

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
    
    public String direccion;
    public Region region;
    public Date fechaCreacion;
    public TipoCliente tipoCliente;
    
    public Cliente(String id)
    {
        super(id);
    }

    public Cliente(String id, String nombre, String apellido, int edad, String sexo, String descripcion)
    {
        super(id);
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.sexo = sexo;
        this.descripcion = descripcion;
    }

    public Cliente(String id, String nombre, String direccion, Region region, Date fechaCreacion, TipoCliente tipoCliente)
    {
        super(id);
        this.nombre = nombre;
        this.direccion = direccion;
        this.region = region;
        this.fechaCreacion = fechaCreacion;
        this.tipoCliente = tipoCliente;
    }
    
    
    
}
