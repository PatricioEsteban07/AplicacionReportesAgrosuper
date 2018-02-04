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
public class PedidoMaterial extends Recurso
{
    public Pedido pedido;
    public Material material;
    public int cantidadCj;
    public float pesoKg;
    public int pesoNeto;
    
    public PedidoMaterial(String id)
    {
        super(id);
    }

    public PedidoMaterial(String id, Pedido pedido, Material material, int cantidadCj, float pesoKg, int pesoNeto)
    {
        super(id);
        this.pedido = pedido;
        this.material = material;
        this.cantidadCj = cantidadCj;
        this.pesoKg = pesoKg;
        this.pesoNeto = pesoNeto;
    }
    
    
    
    
    
    
}
