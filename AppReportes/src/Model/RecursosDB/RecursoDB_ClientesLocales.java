/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.Cliente;
import Model.ClienteLocal;
import Model.LocalDB;
import Model.Region;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class RecursoDB_ClientesLocales extends RecursoDB
{

    public RecursoDB_ClientesLocales(LocalDB db)
    {
        super("ClientesLocales","SELECT * FROM clienteLocal",db);
    }

    @Override
    public boolean obtenerDatos(HashMap<String,RecursoDB> resources)
    {
        try
        {
            this.connect();
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            Logger.getLogger(RecursoDB_ClientesLocales.class.getName()).log(Level.SEVERE, null, ex);
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
                
                String idRegion = result.getString("region_id");//id
                Region regionAux = (this.db.regiones.containsKey(idRegion)) ? 
                        this.db.regiones.get(idRegion)
                        : this.db.regiones.put(idRegion, new Region(idRegion));
                
                String idCliente = result.getString("cliente_id");//id
                Cliente clienteAux = (this.db.clientes.containsKey(idCliente)) ? 
                        this.db.clientes.get(idCliente)
                        : this.db.clientes.put(idCliente, new Cliente(idCliente));
                
                if(!this.db.clientesLocales.containsKey(idAux))
                {
                    ClienteLocal aux = new ClienteLocal(idAux,nombreAux,direccionAux,regionAux,clienteAux);
                    this.add(aux);
                    this.db.clientesLocales.put(idAux, aux);
                }
                else
                {
                    this.add(this.db.clientesLocales.get(idAux));
                }
            }
            this.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RecursoDB_ClientesLocales.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}