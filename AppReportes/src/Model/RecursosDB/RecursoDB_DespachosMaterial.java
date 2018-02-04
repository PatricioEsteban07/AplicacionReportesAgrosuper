/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.Despacho;
import Model.DespachoMaterial;
import Model.LocalDB;
import Model.Material;
import Model.Pedido;
import Model.PedidoMaterial;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class RecursoDB_DespachosMaterial extends RecursoDB
{

    public RecursoDB_DespachosMaterial(LocalDB db)
    {
        super("DespachosMaterial","SELECT * FROM despacho_material",db);
    }

    @Override
    public boolean obtenerDatos(HashMap<String, RecursoDB> resources)
    {
        try
        {
            this.connect();
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            Logger.getLogger(RecursoDB_Empresas.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            ResultSet result = super.executeQuery();

            //recorrer result para crear objetos
            while (result != null && result.next())
            {
                String idAux = result.getString("id");
                int despachoCjAux = result.getInt("despachoCj");
                int despachoKgAux = result.getInt("despachoKg");
                
                String idDespacho = result.getString("despacho_id");//id
                Despacho despachoAux = (this.db.despachos.containsKey(idDespacho)) ? 
                        this.db.despachos.get(idDespacho)
                        : this.db.despachos.put(idDespacho, new Despacho(idDespacho));
                
                String idMaterial = result.getString("material_id");//id
                Material materialAux = (this.db.materiales.containsKey(idMaterial)) ? 
                        this.db.materiales.get(idMaterial)
                        : this.db.materiales.put(idMaterial, new Material(idMaterial));
                
                if(this.db.despachos.containsKey(idDespacho))
                {
                    if(!this.db.despachos.get(idDespacho).materiales.containsKey(idAux))
                    {
                        DespachoMaterial aux = new DespachoMaterial(idAux,despachoAux,materialAux,despachoCjAux,despachoKgAux);
                        this.add(aux);
                        this.db.despachos.get(idDespacho).materiales.put(idAux,aux);
                    }
                    else
                    {
                        this.add(this.db.despachos.get(idDespacho).materiales.get(idAux));
                    }
                }
                else
                {
                    System.out.println("OJO, se obuvo despacho-material pero no se tiene el despacho !");
                }
  
            }
            this.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RecursoDB_Empresas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}
