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
public class ClienteLocal extends Recurso
{
    public String nombre;
    public String direccion;
    public Region region;
    public Cliente cliente;
    
    public ClienteLocal(String id)
    {
        super(id);
    }

    public ClienteLocal(String id, String nombre, String direccion, Region region, Cliente cliente)
    {
        super(id);
        this.nombre = nombre;
        this.direccion = direccion;
        this.region = region;
        this.cliente = cliente;
    }
    
    
    
}
