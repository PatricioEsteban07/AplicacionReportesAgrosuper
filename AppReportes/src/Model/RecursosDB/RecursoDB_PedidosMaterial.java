/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

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
public class RecursoDB_PedidosMaterial extends RecursoDB
{

    public RecursoDB_PedidosMaterial(LocalDB db)
    {
        super("PedidosMaterial","SELECT * FROM pedido_material",db);
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
                int cantCjAux = result.getInt("cantidadCj");
                float pesoAux = result.getFloat("pesoKg");
                int precioAux = result.getInt("precioNeto");
                
                String idPedido = result.getString("pedido_id");//id
                Pedido pedidoAux = (this.db.pedidos.containsKey(idPedido)) ? 
                        this.db.pedidos.get(idPedido)
                        : this.db.pedidos.put(idPedido, new Pedido(idPedido));
                
                String idMaterial = result.getString("material_id");//id
                Material materialAux = (this.db.materiales.containsKey(idMaterial)) ? 
                        this.db.materiales.get(idMaterial)
                        : this.db.materiales.put(idMaterial, new Material(idMaterial));
                
                if(this.db.pedidos.containsKey(idPedido))
                {
                    if(!this.db.pedidos.get(idPedido).materiales.containsKey(idAux))
                    {
                        PedidoMaterial aux = new PedidoMaterial(idAux,pedidoAux,materialAux,cantCjAux,pesoAux,precioAux);
                        this.add(aux);
                        this.db.pedidos.get(idPedido).materiales.put(idAux,aux);
                    }
                    else
                    {
                        this.add(this.db.pedidos.get(idPedido).materiales.get(idAux));
                    }
                }
                else
                {
                    System.out.println("OJO, se obuvo pedido-material pero no se tiene el pedido !");
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
