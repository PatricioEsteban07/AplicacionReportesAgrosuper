/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.Centro;
import Model.LocalDB;
import Model.OficinaVentas;
import Model.Pedido;
import Model.TipoCliente;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class RecursoDB_Pedidos extends RecursoDB
{

    public RecursoDB_Pedidos(LocalDB db)
    {
        super("Pedidos","SELECT * FROM pedido",db);
    }

    @Override
    public boolean obtenerDatos()
    {
        try
        {
            this.connect();
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            Logger.getLogger(RecursoDB_Pedidos.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            ResultSet result = super.executeQuery();

            //recorrer result para crear objetos
            while (result != null && result.next())
            {
                String idAux = result.getString("id");
                Date fechaAux = result.getDate("fechaDoc");
                Date fechaEntregaAux = result.getDate("fechaEntrega");
                
                String idCentro = result.getString("centro_id");//id
                Centro centroAux = (this.db.centros.containsKey(idCentro)) ? 
                        this.db.centros.get(idCentro)
                        : this.db.centros.put(idCentro, new Centro(idCentro));
                
                String idOficina = result.getString("oficina_id");//id
                OficinaVentas oficinaAux = (this.db.oficinas.containsKey(idOficina)) ? 
                        this.db.oficinas.get(idOficina)
                        : this.db.oficinas.put(idOficina, new OficinaVentas(idOficina));
                
                String idtipoCliente = result.getString("tipoCliente_id");//id
                TipoCliente tipoClienteAux = (this.db.tiposClientes.containsKey(idtipoCliente)) ? 
                        this.db.tiposClientes.get(idtipoCliente)
                        : this.db.tiposClientes.put(idtipoCliente, new TipoCliente(idtipoCliente));
                
                if(!this.db.pedidos.containsKey(idAux))
                {
                    Pedido aux = new Pedido(idAux,fechaAux,centroAux,oficinaAux,tipoClienteAux,fechaEntregaAux);
                    this.add(aux);
                    this.db.pedidos.put(idAux, aux);
                }
                else
                {
                    this.add(this.db.pedidos.get(idAux));
                }
            }
            this.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RecursoDB_Pedidos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}
