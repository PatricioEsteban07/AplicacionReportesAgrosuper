/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.Cliente;
import Model.LocalDB;
import Model.Region;
import Model.TipoCliente;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class RecursoDB_Clientes extends RecursoDB
{

    public RecursoDB_Clientes(LocalDB db)
    {
        super("Clientes","SELECT * FROM cliente",db);
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
            Logger.getLogger(RecursoDB_Clientes.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            ResultSet result = super.executeQuery();

            //recorrer result para crear objetos
            while (result != null && result.next())
            {
                String idAux = result.getString("id");
                String nombreAux = result.getString("nombre");
                String direccionAux = result.getString("direccion");
                Date fechaAux = result.getDate("fechaCreacion");
                
                String idRegion = result.getString("region_id");//id
                Region regionAux = (this.db.regiones.containsKey(idRegion)) ? 
                        this.db.regiones.get(idRegion)
                        : this.db.regiones.put(idRegion, new Region(idRegion));
                
                String idtipoCliente = result.getString("tipoCliente_id");//id
                TipoCliente tipoClienteAux = (this.db.tiposClientes.containsKey(idtipoCliente)) ? 
                        this.db.tiposClientes.get(idtipoCliente)
                        : this.db.tiposClientes.put(idtipoCliente, new TipoCliente(idtipoCliente));
                
                if(!this.db.clientes.containsKey(idAux))
                {
                    Cliente aux = new Cliente(idAux,nombreAux,direccionAux,regionAux,fechaAux,tipoClienteAux);
                    this.add(aux);
                    this.db.clientes.put(idAux, aux);
                }
                else
                {
                    this.add(this.db.clientes.get(idAux));
                }
            }
            this.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RecursoDB_Clientes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}