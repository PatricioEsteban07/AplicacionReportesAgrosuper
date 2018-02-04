/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Patricio
 */
public class Despacho extends Recurso
{
    public Centro centro;
    public Date fecha;
    public ClienteLocal clienteLocal;
    public HashMap<String,DespachoMaterial> materiales;
    
    public Despacho(String id)
    {
        super(id);
    }

    public Despacho(String id, Centro centro, Date fecha, ClienteLocal clienteLocal)
    {
        super(id);
        this.centro = centro;
        this.fecha = fecha;
        this.clienteLocal = clienteLocal;
        this.materiales=new HashMap<>();
    }
    
    
}
