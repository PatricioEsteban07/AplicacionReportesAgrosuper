/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Patricio
 */
public class Pedido extends Recurso
{
    public Date fechaDoc;
    public Centro centro;
    public OficinaVentas oficina;
    public TipoCliente tipoCliente;
    public Date fechaEntrega;
    public HashMap<String,PedidoMaterial> materiales;
    
    public Pedido(String id)
    {
        super(id);
    }

    public Pedido(String id, Date fechaDoc, Centro centro, OficinaVentas oficina, TipoCliente tipoCliente, Date fechaEntrega)
    {
        super(id);
        this.fechaDoc = fechaDoc;
        this.centro = centro;
        this.oficina = oficina;
        this.tipoCliente = tipoCliente;
        this.fechaEntrega = fechaEntrega;
        this.materiales=new HashMap<>();
    }
    
    
    
    
}
