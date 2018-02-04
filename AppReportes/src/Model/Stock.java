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
public class Stock extends Recurso
{
    public Centro centro;
    public Date fecha;
    public Material material;
    public float salidas;
    public float stock;
    public float disponible;
    
    public Stock(String id)
    {
        super(id);
    }

    public Stock(String id, Centro centro, Date fecha, Material material, float salidas, float stock, float disponible)
    {
        super(id);
        this.centro = centro;
        this.fecha = fecha;
        this.material = material;
        this.salidas = salidas;
        this.stock = stock;
        this.disponible = disponible;
    }
    
    
}
