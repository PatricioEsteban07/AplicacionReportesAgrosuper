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
public class DespachoMaterial extends Recurso
{
    public Despacho despacho;
    public Material material;
    public int despachoCj;
    public int despachoKg;
    
    public DespachoMaterial(String id)
    {
        super(id);
    }

    public DespachoMaterial(String id, Despacho despacho, Material material, int despachoCj, int despachoKg)
    {
        super(id);
        this.despacho = despacho;
        this.material = material;
        this.despachoCj = despachoCj;
        this.despachoKg = despachoKg;
    }
    
    
}
